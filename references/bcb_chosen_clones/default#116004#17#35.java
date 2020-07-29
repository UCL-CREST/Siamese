    public Object copy(Object obj) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class classType = obj.getClass();
        System.out.println("该对象的类型是：" + classType.toString());
        Object objectCopy = classType.getConstructor(new Class[] {}).newInstance(new Object[] {});
        Field[] fields = classType.getDeclaredFields();
        for (int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            String fieldName = field.getName();
            String stringLetter = fieldName.substring(0, 1).toUpperCase();
            String getName = "get" + stringLetter + fieldName.substring(1);
            String setName = "set" + stringLetter + fieldName.substring(1);
            Method getMethod = classType.getMethod(getName, new Class[] {});
            Method setMethod = classType.getMethod(setName, new Class[] { field.getType() });
            Object value = getMethod.invoke(obj, new Object[] {});
            System.out.println(fieldName + " :" + value);
            setMethod.invoke(objectCopy, new Object[] { value });
        }
        return objectCopy;
    }
