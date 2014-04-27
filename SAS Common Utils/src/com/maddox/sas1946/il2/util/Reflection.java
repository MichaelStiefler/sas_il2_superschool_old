package com.maddox.sas1946.il2.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {
	public static Object genericInvokeMethod(Object obj, String methodName) {
		return genericInvokeMethod(obj, methodName, new Object[]{});
	}
	public static Object genericInvokeMethod(Object obj, String methodName, Object[] params) {
        Method method = null;
        Object requiredObj = null;
        Class[] classArray = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            classArray[i] = params[i].getClass();
        }
        Class objClass = obj.getClass();
        do {
        	if (objClass == null) break;
	        try {
        		method = objClass.getDeclaredMethod(methodName, classArray);
	            method.setAccessible(true);
	        } catch (Exception e) {
	        }
	        objClass = objClass.getSuperclass();
        } while (method == null);
        
        if (method != null) {
	        try {
	            requiredObj = method.invoke(obj, params);
	        } catch (IllegalArgumentException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        } catch (InvocationTargetException e) {
	            e.printStackTrace();
	        }
        }

        return requiredObj;
    }
}
