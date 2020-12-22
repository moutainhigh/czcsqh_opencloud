import com.opencloud.generator.server.service.GenerateConfig;
import com.opencloud.generator.server.service.GeneratorService;

import java.io.File;

public class GeneratorMain {

    public static void main(String[] args) {
        String outputDir = System.getProperty("user.dir") + File.separator + "generator";
        GenerateConfig config = new GenerateConfig();
        config.setJdbcUrl("spring.datasource.url=jdbc:mysql://39.99.239.190:3306/openCloud?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai");
        config.setJdbcUserName("root");
        config.setJdbcPassword("Yan_5201314");
        config.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        config.setAuthor("yanjiajun");
        config.setParentPackage("com.opencloud");
        config.setModuleName("base");
        config.setIncludeTables(new String[]{"global_config"});
        config.setTablePrefix(new String[]{""});
        config.setOutputDir(outputDir);
        GeneratorService.execute(config);
    }

}
