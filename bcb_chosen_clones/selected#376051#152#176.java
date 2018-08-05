    public Object clone(Object list) {
        if (this.isArray(list)) {
            Class type = list.getClass().getComponentType();
            int length = Array.getLength(list);
            Object clone = Array.newInstance(type, length);
            System.arraycopy(list, 0, clone, 0, length);
            return clone;
        }
        if (!this.isList(list)) {
            return null;
        }
        Class clazz = list.getClass();
        try {
            Method cloneMethod = clazz.getMethod("clone", new Class[0]);
            return cloneMethod.invoke(list, null);
        } catch (Exception ignoreAndTryTheNextStep) {
        }
        try {
            List clone = (List) clazz.newInstance();
            clone.addAll(((List) list));
            return clone;
        } catch (Exception ignoreAndTryTheNextStep) {
        }
        return new ArrayList(((List) list));
    }
