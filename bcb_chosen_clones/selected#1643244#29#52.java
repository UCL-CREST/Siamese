    public Object fn(Object arg0, Object arg1) {
        if (arg0 == null || arg1 == null) {
            return null;
        }
        Class arg0Type = arg0.getClass();
        Class arg1Type = arg1.getClass();
        if (arg0Type.isArray() && arg1Type.isArray() && arg0Type.getComponentType() == arg1Type.getComponentType()) {
            int arg0Size = Array.getLength(arg0);
            int arg1Size = Array.getLength(arg1);
            Object array = Array.newInstance(arg0Type.getComponentType(), arg0Size + arg1Size);
            System.arraycopy(arg0, 0, array, 0, arg0Size);
            System.arraycopy(arg1, 0, array, arg0Size, arg1Size);
            return array;
        }
        if (List.class.isAssignableFrom(arg0Type) && List.class.isAssignableFrom(arg1Type)) {
            List arg0List = (List) arg0;
            List arg1List = (List) arg1;
            List list = new ArrayList(arg0List.size() + arg1List.size());
            list.addAll(arg0List);
            list.addAll(arg1List);
            return list;
        }
        throw new IllegalArgumentException("Both arguments must be a List or an array");
    }
