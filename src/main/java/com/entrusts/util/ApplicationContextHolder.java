package com.entrusts.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {

	private static ApplicationContext applicationContext = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextHolder.applicationContext = applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		assertContextNotNull();
		return (T) applicationContext.getBean(name);
	}

	public static <T> T getBean(Class<T> type) {
		assertContextNotNull();
		return applicationContext.getBean(type);
	}

	private static void assertContextNotNull() {
		if (applicationContext == null) {
			throw new IllegalStateException("获取上下文失败");
		}
	}
}
