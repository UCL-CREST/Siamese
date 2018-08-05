    public static Object[] sort(Object[] a, Comparator c) {
        Class componentType = a.getClass().getComponentType();
        Object[] target = (Object[]) Array.newInstance(componentType, a.length);
        System.arraycopy(a, 0, target, 0, a.length);
        Arrays.sort(target, c);
        return target;
    }
