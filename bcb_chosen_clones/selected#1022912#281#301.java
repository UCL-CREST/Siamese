    private static Object setIndexInList(Object bean, int idx, Object child) {
        if (bean instanceof List) {
            List l = (List) bean;
            while (l.size() <= idx) {
                l.add(null);
            }
            l.set(idx, child);
            return l;
        } else if (bean.getClass().isArray()) {
            int length = Array.getLength(bean);
            if (length <= idx) {
                Object newArray = Array.newInstance(bean.getClass().getComponentType(), idx + 1);
                System.arraycopy(bean, 0, newArray, 0, length);
                bean = newArray;
            }
            Array.set(bean, idx, child);
            return bean;
        } else {
            return null;
        }
    }
