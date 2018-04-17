    public Object getAttribute(String name) {
        Object obj = m_attrMap.get(name);
        if ((obj != null) && obj.getClass().isArray()) {
            Class clazz = obj.getClass().getComponentType();
            int len = Array.getLength(obj);
            Object copy = Array.newInstance(obj.getClass().getComponentType(), len);
            System.arraycopy(obj, 0, copy, 0, len);
            obj = copy;
        }
        return obj;
    }
