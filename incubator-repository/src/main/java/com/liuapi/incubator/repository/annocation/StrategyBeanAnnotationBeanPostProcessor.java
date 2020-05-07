package com.liuapi.incubator.repository.annocation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Note: 需要将该Bean工厂后置处理器导入到 Ioc容器中，需要借助@Import导入的注册机
 * Description: 该工厂后置处理器会扫描指定包下添加了自定义注解Strategy的类，并将这些类动态加载到Ioc容器中
 */
@Slf4j
public class StrategyBeanAnnotationBeanPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware,
        ResourceLoaderAware {
    private final Set<String> packagesToScan;
    private Environment environment;
    private ResourceLoader resourceLoader;

    public StrategyBeanAnnotationBeanPostProcessor(Collection<String> packagesToScan) {
        this.packagesToScan = new LinkedHashSet<>(packagesToScan);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ClassPathBeanDefinitionScanner scanner =
                new ClassPathBeanDefinitionScanner(registry, false, environment, resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(StrategyBean.class));
        if (CollectionUtils.isEmpty(packagesToScan)) {
            return;
        }
        packagesToScan.stream()
                .forEach(
                        packageToScan -> {
                            int scan = scanner.scan(packageToScan);
                            if (0 == scan) {
                                log.warn("packagesToScan is empty , {} registry will be ignored!", StrategyBean.class.getSimpleName());
                            } else {
                                log.info("Load {} bean with Annotation {} in package {}", scan, StrategyBean.class.getSimpleName(), packageToScan);
                            }
                        }
                );

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
