	public static void main(String args[]) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Integer[] params = {};
		Class[] types = {};
		Object retval = CallMethodUsingReflection.callMethod3(new GCD(), "getClass", (Object []) params, types);
		System.out.println(retval);
	}
