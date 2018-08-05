	public static Object instantiate(String clazz, Object[] pvalues, Class[] ptypes) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> c = Class.forName(clazz);
		Constructor<?> constructor = c.getConstructor(ptypes);
		Object obj = constructor.newInstance(pvalues);
		return obj;
	}
