package com.opencloud.base.server.service.feign;

import com.opencloud.base.client.constants.AdminConstants;
import com.opencloud.base.client.service.ILoginServiceClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author: jiajun.yan
 * @date: 2020/2/19 14.35
 * @description:
 */
@Component
@FeignClient(value = AdminConstants.ADMIN_SERVICE)
public interface LoginServiceClient extends ILoginServiceClient {

}