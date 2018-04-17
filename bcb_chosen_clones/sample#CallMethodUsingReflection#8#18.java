	public static Object callMethod1(Object object, String methodName, Object args[]) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//Caveat: this solution doesn't support some method signatures (e.g., those with primitive types)
		Class<?> myClass = object.getClass();
		Class<?>[] ptypes = new Class[args.length];
		for(int i = 0; i < args.length; i++) {
			ptypes[i] = args[i].getClass();
		}
		Method method = myClass.getMethod(methodName, ptypes);
		Object returnVal = method.invoke(object, args);
		return returnVal;
	}
