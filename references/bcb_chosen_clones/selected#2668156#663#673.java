    public static Object newInstance(Class pClassType, Object[] pParameterValues) {
        Constructor constructor = getConstructor(pClassType, pParameterValues);
        if (constructor == null) {
            Class[] parameterTypes = new Class[pParameterValues.length];
            for (int i = 0, n = pParameterValues.length; i < n; i++) {
                parameterTypes[i] = pParameterValues[i] != null ? pParameterValues[i].getClass() : null;
            }
            throw new IllegalArgumentException("unknown constructor: " + formatMethodName(pClassType, pClassType.getName(), parameterTypes));
        }
        return newInstance(constructor, pParameterValues);
    }
