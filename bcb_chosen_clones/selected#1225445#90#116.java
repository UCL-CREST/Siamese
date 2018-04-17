    protected static Object convert(Object anObject, Class aClass) {
        Constructor[] ctors = aClass.getConstructors();
        Class[] types;
        for (int i = 0; i < ctors.length; i++) {
            types = ctors[i].getParameterTypes();
            if (types.length == 1) {
                if (types[0].equals(anObject.getClass())) {
                    try {
                        return ctors[i].newInstance(new Object[] { anObject });
                    } catch (Exception exc) {
                    }
                }
            }
        }
        for (int i = 0; i < ctors.length; i++) {
            types = ctors[i].getParameterTypes();
            if (types.length == 1) {
                if (anObject.getClass().isAssignableFrom(types[0])) {
                    try {
                        return ctors[i].newInstance(new Object[] { anObject });
                    } catch (Exception exc) {
                    }
                }
            }
        }
        return null;
    }
