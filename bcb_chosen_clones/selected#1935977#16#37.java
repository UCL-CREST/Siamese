    public static Class[] getSuperTypes(Class c) {
        if (c.isPrimitive()) return (Class[]) primitives.get(c);
        if (c.isArray()) {
            Class type = c.getComponentType();
            if (type == OBJECT_CLASS || type.isPrimitive()) return ARRAY_OBJECT_SUPERTYPES; else {
                Class[] supertypes = getSuperTypes(type);
                int length = supertypes.length;
                for (int i = 0; i < length; i++) supertypes[i] = Array.newInstance(supertypes[i], 0).getClass();
                return supertypes;
            }
        }
        Class[] interfaces = c.getInterfaces();
        int length = interfaces.length;
        if (c.isInterface()) if (length == 0) return OBJECT_SUPERTYPES; else return interfaces;
        Class superclass = c.getSuperclass();
        if (superclass == null) return interfaces; else {
            Class[] classes = new Class[length + 1];
            System.arraycopy(interfaces, 0, classes, 1, length);
            classes[0] = superclass;
            return classes;
        }
    }
