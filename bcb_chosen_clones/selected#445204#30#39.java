    public static Object[] newInstances(String[] types, String[] values) throws ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Object[] out = null;
        if ((types != null) && (values != null) && (types.length == values.length)) {
            out = new Object[types.length];
            for (int i = 0; i < types.length; i++) {
                out[i] = Class.forName(types[i].trim()).getConstructor(String.class).newInstance(values[i]);
            }
        }
        return out;
    }
