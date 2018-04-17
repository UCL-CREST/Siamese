    public static Object[] concat(Object[][] lsts) {
        int totLength = 0;
        Class<?> componentType = null;
        for (int i = 0; i < lsts.length; i++) {
            Object[] lst = lsts[i];
            if (lst == null) continue;
            if (componentType == null) componentType = lst.getClass().getComponentType(); else {
                Class<?> newComponentType = lst.getClass().getComponentType();
                if (newComponentType != componentType) {
                    if (!componentType.isAssignableFrom(newComponentType)) {
                        if (newComponentType.isAssignableFrom(componentType)) {
                            componentType = newComponentType;
                        } else {
                            componentType = Object.class;
                        }
                    }
                }
            }
            totLength += lst.length;
        }
        if (componentType == null) {
            return new Object[0];
        }
        Object[] l = (Object[]) Array.newInstance(componentType, totLength);
        int offset = 0;
        for (int i = 0; i < lsts.length; i++) {
            Object[] lst = lsts[i];
            if (lst == null) continue;
            System.arraycopy(lst, 0, l, offset, lst.length);
            offset += lst.length;
        }
        return l;
    }
