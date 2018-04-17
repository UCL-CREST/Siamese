	public static Object callMethod2(Object object, String methodName, Object params[], Class[] types) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Uses getMethod
		Class<?> myClass = object.getClass();
		Method method = myClass.getMethod(methodName, types);
		Object returnVal = method.invoke(object, params);
		return returnVal;
	}
