package org.cyg.thinking.in.spring.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步事件处理器示例
 */
public class AsyncEventHandlerDemo {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();

        // 1.添加自定义 Spring 事件监听器
        context.addApplicationListener(new MySpringEventListener());

        // 2.启动 Spring 应用上下文
        context.refresh(); // 初始化 ApplicationEventMulticaster

        // 依赖查找
        ApplicationEventMulticaster applicationEventMulticaster =
                context.getBean(AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
        // 判断当前 ApplicationEventMulticaster 是否为 SimpleApplicationEventMulticaster
        if (applicationEventMulticaster instanceof SimpleApplicationEventMulticaster) {
            // 切换 taskExecutor
            SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = (SimpleApplicationEventMulticaster) applicationEventMulticaster;

            ExecutorService taskExecutor = Executors.newSingleThreadExecutor(new CustomizableThreadFactory("my-spring-event-thread-pool"));
            // 同步 -> 异步
            simpleApplicationEventMulticaster.setTaskExecutor(taskExecutor);

            // 添加 ContextCloseEvent 事件处理
            simpleApplicationEventMulticaster.addApplicationListener((ApplicationListener<ContextClosedEvent>) event -> {
                System.out.println("ContextClosed");
                if (!taskExecutor.isShutdown()) {
                    taskExecutor.shutdown();
                }
            });

            simpleApplicationEventMulticaster.setErrorHandler(e -> {
                System.err.println("当 Spring 事件异常时, 原因:" + e.getMessage());
            });
        }
        context.addApplicationListener((ApplicationListener<MySpringEvent>) event -> {
            throw new RuntimeException("故意抛出异常");
        });
        // 3.发布自定义 Spring 事件
        context.publishEvent(new MySpringEvent("Hello, World!!"));

        // 4.关闭 Spring 应用上下文 (ContextClosedEvent)
        context.close();
    }
}
