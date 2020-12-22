package com.opencloud.base.server.service.impl.sale;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.opencloud.base.client.model.entity.sale.ProductItemPackage;
import com.opencloud.base.client.model.entity.sale.UserProductItem;
import com.opencloud.base.server.mapper.sale.UserProductItemMapper;
import com.opencloud.base.server.service.sale.ProductItemPackageService;
import com.opencloud.base.server.service.sale.UserProductItemService;
import com.opencloud.common.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户产品表 服务实现类
 *
 * @author liyueping
 * @date 2019-11-29
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserProductItemServiceImpl extends BaseServiceImpl<UserProductItemMapper, UserProductItem> implements UserProductItemService {

    @Autowired
    private UserProductItemMapper userProductItemMapper;
    @Autowired
    private ProductItemPackageService productItemPackageService;

    /**
     * 2020-4-20,本方法废除，固定返回0
     * 查询用户试纸总数
     *
     * @param id
     * @return
     */
    @Override
    public Integer getProductTotalByUserId(Long id) {
            return 0;
    }

    @Override
    public void addWhenBindingDevice(Long productPackageId, Long deviceId ,Long userId ,Long orderId) {
        QueryWrapper<ProductItemPackage> productItemPackageQueryWrapper = new QueryWrapper<>();
        productItemPackageQueryWrapper.lambda().eq(ProductItemPackage::getProductPackageId, productPackageId);
        List<ProductItemPackage> productItemPackageList = productItemPackageService.list(productItemPackageQueryWrapper);

        List<ProductItemPackage> haveFreeProductItemList = new ArrayList<>();
        for (int i = 0; i < productItemPackageList.size(); i++) {
            ProductItemPackage temp = productItemPackageList.get(i);
            if (temp.getProductItemNum() > temp.getProductItemInitNum()) {
                haveFreeProductItemList.add(temp);
            }
        }
        List<UserProductItem> saveAddList = new ArrayList<>();
        for (int i = 0; i < haveFreeProductItemList.size(); i++) {
            ProductItemPackage temp = haveFreeProductItemList.get(i);
            if(temp.getProductItemNum() > 0){
                UserProductItem add = new UserProductItem();
                add.setUserId(userId);
                add.setProductItemId(temp.getProductItemId());
                add.setDeviceId(deviceId);
                add.setProductItemNum(temp.getProductItemNum());
                add.setNumStatus(1);
                add.setParentId(0L);
                add.setStatus(1);
                add.setOrderId(orderId);
                saveAddList.add(add);
            }
        }
        this.saveBatch(saveAddList);

        //如果有初始发货数量的话，就要额外再增加一条减的
        List<UserProductItem> saveSubList = new ArrayList<>();
        for (int i = 0; i < haveFreeProductItemList.size(); i++) {
            ProductItemPackage temp = haveFreeProductItemList.get(i);
            if(temp.getProductItemInitNum() > 0){
                UserProductItem sub = new UserProductItem();
                sub.setUserId(userId);
                sub.setProductItemId(temp.getProductItemId());
                sub.setDeviceId(deviceId);
                sub.setProductItemNum(-temp.getProductItemInitNum());
                sub.setNumStatus(0);
                sub.setParentId(0L);
                sub.setStatus(1);
                sub.setOrderId(orderId);
                //这里是重点，找parent
                for(int j =0;j<saveAddList.size();j++){
                    UserProductItem add = saveAddList.get(j);
                    if(add.getUserId().equals(sub.getUserId()) && add.getProductItemId().equals(sub.getProductItemId()) && add.getDeviceId().equals(sub.getDeviceId())){
                        sub.setParentId(add.getUserProductItemId());
                        break;
                    }
                }
                saveSubList.add(sub);
            }
        }
        this.saveBatch(saveSubList);
    }

    @Override
    public void delWhenReturnDevice(Long userId, Long deviceId, Long productPackageId) {
        //先用productPackageId查出来对应的productItemId
        if(productPackageId == null){
            //如果没有套餐id的话，也就意味着不需要去清除产品余额
            return;
        }

        QueryWrapper<ProductItemPackage> productItemPackageQueryWrapper = new QueryWrapper<>();
        productItemPackageQueryWrapper.lambda().eq(ProductItemPackage::getProductPackageId, productPackageId);
        List<ProductItemPackage> productItemPackageList = productItemPackageService.list(productItemPackageQueryWrapper);
        List<Long> productItemIds = productItemPackageList.stream().map(ProductItemPackage::getProductItemId).collect(Collectors.toList());

        UpdateWrapper<UserProductItem> userProductItemUpdateWrapper = new UpdateWrapper<>();
        userProductItemUpdateWrapper.lambda().eq(UserProductItem::getUserId, userId).eq(UserProductItem::getDeviceId, deviceId).in(productItemIds.size() > 0, UserProductItem::getProductItemId, productItemIds);
        userProductItemMapper.delete(userProductItemUpdateWrapper);
    }

    @Override
    public void exchangeDeviceId(Long oldDeviceId, Long newDeviceId) {
        QueryWrapper<UserProductItem> userProductItemQueryWrapper = new QueryWrapper<>();
        userProductItemQueryWrapper.lambda().eq(UserProductItem::getDeviceId, oldDeviceId);
        List<UserProductItem> userProductItemList = this.list(userProductItemQueryWrapper);
        if (userProductItemList.size() > 0) {
            userProductItemList.forEach(item -> {
                item.setDeviceId(newDeviceId);
            });
            this.updateBatchById(userProductItemList);
        }
    }

    /**
     * Excel查询剩余试纸数
     * @param deviceIds
     * @return
     */
    @Override
    public Map<Long, List<Object>> surplusProductItemNum(List<Long> deviceIds) {
        Map<Long,List<Object>> map = new HashMap<>();

//        //计算365天前的时间内
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(new Date());
//        cal.add(Calendar.DATE, -365);
//        Date expTime = cal.getTime();
//        QueryWrapper<UserProductItem> userProductItemQueryWrapper = new QueryWrapper<>();
//        /**
//         * 365天内，itemNum正数
//         */
//        userProductItemQueryWrapper.lambda()
//                .ge(UserProductItem::getCreateTime,expTime)
//                .in(UserProductItem::getDeviceId,deviceIds)
//                .gt(UserProductItem::getProductItemNum,0);
//        List<UserProductItem> list = this.list(userProductItemQueryWrapper);
//        if(list.isEmpty()) { return null;}
//        List<Long> productItemIds = list.stream().map(UserProductItem::getUserProductItemId).collect(Collectors.toList());
//        QueryWrapper<UserProductItem> itemQueryWrapper = new QueryWrapper<>();
//        itemQueryWrapper.lambda().in(UserProductItem::getParentId,productItemIds);
//        List<UserProductItem> byParentLists = this.list(itemQueryWrapper);
//        if(byParentLists == null) { return null;}
//        for (int i = 0; i < byParentLists.size(); i++) {
//            UserProductItem byParentList = byParentLists.get(i);
//            for (int j = 0; j < list.size(); j++) {
//                UserProductItem userProductItem = list.get(j);
//                if(byParentList.getParentId().equals(userProductItem.getUserProductItemId())) {
//                    List<Object> valList = new ArrayList<>();
//                    String item =String.valueOf(byParentList.getProductItemNum() + userProductItem.getProductItemNum());
//                    valList.add(item);
//                    valList.add(userProductItem.getCreateTime());
//                    map.put(byParentList.getDeviceId(),valList);
//                }
//            }
//        }
        QueryWrapper<UserProductItem> userProductItemQueryWrapper = new QueryWrapper<>();
        userProductItemQueryWrapper.lambda()
                .in(UserProductItem::getDeviceId,deviceIds)
                .groupBy(UserProductItem::getDeviceId);
        userProductItemQueryWrapper.select("device_id,create_time,sum(product_item_num) as product_item_num");
        List<UserProductItem> list = this.list(userProductItemQueryWrapper);
        if(list.isEmpty()) { return null;}
        for (UserProductItem userProductItem : list) {
            List<Object> valList = new ArrayList<>();
            valList.add(userProductItem.getProductItemNum());
            valList.add(userProductItem.getCreateTime());
            map.put(userProductItem.getDeviceId(),valList);
        }
        return map;
    }

    @Override
    public void dualExpiredProductItem() {
        //本方法创建于2020-06-11，用于定时任务调用，处理过期的免费试纸
        //主要的逻辑：找到365天前的那天，找到当天所有num_status=1的记录。分别对每一条记录，查出它们的num_status=0的子记录
        //统计出剩余的没有领取的试纸的数量，然后系统插入一条领取剩余所有试纸的记录，这样就可以做成[使试纸过期]的现象
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 365);
        Date targetDay = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String targetDayStr = sdf.format(targetDay);

        QueryWrapper<UserProductItem> userProductItemQueryWrapper = new QueryWrapper<>();
        userProductItemQueryWrapper.lambda().eq(UserProductItem::getNumStatus,1).likeRight(UserProductItem::getCreateTime,targetDayStr);
        List<UserProductItem> userProductItemList = this.list(userProductItemQueryWrapper);
        List<UserProductItem> saveList = new ArrayList<>();
        List<UserProductItem> updateList = new ArrayList<>();
        List<UserProductItem> childList = new ArrayList<>();
        if(userProductItemList.size() > 0) {
            //找出userProductItemList中记录的所有子记录
            List<Long> userProductItemIdList = userProductItemList.stream().map(UserProductItem::getUserProductItemId).collect(Collectors.toList());
            userProductItemQueryWrapper = new QueryWrapper<>();
            userProductItemQueryWrapper.lambda().in(UserProductItem::getParentId,userProductItemIdList);
            childList = this.list(userProductItemQueryWrapper);
            for(int i=0;i<userProductItemList.size();i++){
                UserProductItem tempParent = userProductItemList.get(i);
                Integer total = tempParent.getProductItemNum();
                Integer get = 0;
                Integer balance = 0;
                for(int j=0;j<childList.size();j++){
                    UserProductItem tempChild = childList.get(j);
                    if(tempChild.getParentId().equals(tempParent.getUserProductItemId())){
                        get += tempChild.getProductItemNum();
                    }
                }
                //计算出剩余的试纸数量
                balance = total + get ;
                UserProductItem add = new UserProductItem();
                add.setUserId(tempParent.getUserId());
                add.setProductItemId(tempParent.getProductItemId());
                add.setDeviceId(tempParent.getDeviceId());
                add.setParentId(tempParent.getUserProductItemId());
                add.setProductItemNum(-balance);
                add.setNumStatus(0);
                add.setOperator("system");
                add.setStatus(1);
                saveList.add(add);
                //然后把父记录的状态改成0，更新一下
                tempParent.setNumStatus(0);
                updateList.add(tempParent);
            }
            this.saveBatch(saveList);
            this.updateBatchById(updateList);
        }
    }

    @Override
    public List<UserProductItem> getUserProductItemByDeviceId(Long deviceId) {
        QueryWrapper<UserProductItem> userProductItemQueryWrapper = new QueryWrapper<>();
        userProductItemQueryWrapper.lambda().eq(UserProductItem::getDeviceId, deviceId).orderByDesc(UserProductItem::getCreateTime);
        List<UserProductItem> childList = this.list(userProductItemQueryWrapper);
        return childList;
    }
}
