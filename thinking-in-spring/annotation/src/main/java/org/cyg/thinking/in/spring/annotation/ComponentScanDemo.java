package org.cyg.thinking.in.spring.annotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * {@link ComponentScan} 扫描示例
 *
 * @see Component
 * @see ComponentScan
 */
// basePackages() @AliasFor value()
// value() @AliasFor basePackages()
@MyComponentScan2(basePackages = "org.cyg.thinking.in.spring.annotation") // 指定 Class-Path(s)
//@ComponentScan(value = "org.cyg.thinking.in.spring.annotation") // 指定 Class-Path(s)
public class ComponentScanDemo {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册
        context.register(ComponentScanDemo.class);
        // 启动 Spring 应用上下文
        context.refresh();

        // 依赖查找 TestClass Bean
        // TestClass 标注 @MyComponent2
        // @MyComponent2 <- @MyComponent <- @Component
        // 从 Spring 开始支持多层次 @Component “派生”
        TestClass testClass = context.getBean(TestClass.class);
        System.out.println(testClass);
        // Annotation -> AnnotationAttributes(Map)
        // 关闭 Spring 应用上下文
        context.close();
    }
}
