package cn.qingweico.config;

import com.google.common.base.Optional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author zqw
 * @date 2023/11/11
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Resource
    private SwaggerProperties swaggerProperties;

    @Bean
    public Docket docket() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(swaggerProperties.getEnable())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket userGroupDocket() {
        return userGroupDocket("sysUser");
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .contact(new Contact(swaggerProperties.getName(),
                        swaggerProperties.getUrl(),
                        swaggerProperties.getEmail()))
                .version(swaggerProperties.getVersion())
                .description(swaggerProperties.getDescription())
                .build();
    }

    public Docket userGroupDocket(String groupName) {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(swaggerProperties.getEnable())
                .groupName(groupName)
                .select()
                .apis(selector -> {
                    if (selector.isAnnotatedWith(ApiGroup.class)) {
                        Optional<ApiGroup> apiGroupClass = selector.findAnnotation(ApiGroup.class);
                        if (apiGroupClass.isPresent()) {
                            ApiGroup apiVersion = apiGroupClass.get();
                            if (apiVersion.value() != null && apiVersion.value().length != 0) {
                                if (Arrays.asList(apiVersion.value()).contains(groupName)) {
                                    return true;
                                }
                            }
                        }
                    }
                    Optional<ApiGroup> apiGroupController = selector.findControllerAnnotation(ApiGroup.class);
                    if (apiGroupController.isPresent()) {
                        ApiGroup apiGroup = apiGroupController.get();
                        if (apiGroup.value() != null && apiGroup.value().length != 0) {
                            return Arrays.asList(apiGroup.value()).contains(groupName);
                        }
                    }
                    return false;
                }).paths(PathSelectors.any()).build();
    }
}
