    public static Object getInstance(Class<?> clazz, Object[] params) {
        Object obj = objMap.get(clazz.toString() + params.toString());
        if (obj == null) {
            Class<?>[] parameterTypes = new Class[params.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                parameterTypes[i] = params[i].getClass();
            }
            try {
                obj = clazz.getConstructor(parameterTypes).newInstance(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
            objMap.put(clazz.toString() + params.toString(), obj);
        }
        return obj;
    }
