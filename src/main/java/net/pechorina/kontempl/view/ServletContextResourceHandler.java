package net.pechorina.kontempl.view;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;

public class ServletContextResourceHandler implements InvocationHandler {
	private final ServletContext target;

    private ServletContextResourceHandler(ServletContext target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("getResourceAsStream".equals(method.getName())) {
            Object result = method.invoke(target, args);
            if (result == null) {
                result = ServletContextResourceHandler.class.getResourceAsStream((String) args[0]);
            }
            return result;
        } else if ("getResource".equals(method.getName())) {
            Object result = method.invoke(target, args);
            if (result == null) {
                result = ServletContextResourceHandler.class.getResource((String) args[0]);
            }
            return result;
        }

        return method.invoke(target, args);
    }
}
