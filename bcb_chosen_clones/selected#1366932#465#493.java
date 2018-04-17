    protected Object setAssemblageValue(Class assemblageType, Object assemblage, Object index, Object value) {
        if (assemblageType.isArray()) {
            int i = ((Integer) index).intValue();
            if (Array.getLength(assemblage) <= i) {
                Object newAssemblage = Array.newInstance(assemblageType.getComponentType(), i + 1);
                System.arraycopy(assemblage, 0, newAssemblage, 0, Array.getLength(assemblage));
                assemblage = newAssemblage;
            }
            Array.set(assemblage, i, value);
        } else if (List.class.isAssignableFrom(assemblageType)) {
            int i = ((Integer) index).intValue();
            List list = (List) assemblage;
            if (list.size() > i) {
                list.set(i, value);
            } else {
                while (list.size() < i) {
                    list.add(null);
                }
                list.add(value);
            }
        } else if (Map.class.isAssignableFrom(assemblageType)) {
            ((Map) assemblage).put(index, value);
        } else if (assemblage instanceof Collection) {
            ((Collection) assemblage).add(value);
        } else {
            throw new IllegalArgumentException("assemblage must be of type array, collection or map.");
        }
        return assemblage;
    }
