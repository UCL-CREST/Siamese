    public Object find(Class type, String fieldName, String fieldValue) {
        Class fieldType = null;
        Object fieldObj = null;
        String methodName = null;
        methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
        System.out.println("methodName = " + methodName);
        Method m0 = null;
        try {
            m0 = type.getMethod(methodName);
            System.out.println("methodName from type  = " + m0.getReturnType());
            fieldType = m0.getReturnType();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        try {
            fieldObj = fieldType.getConstructor(String.class).newInstance(fieldValue);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Query query = db.query();
        query.constrain(type);
        query.descend(fieldName).constrain(fieldObj);
        ObjectSet set = query.execute();
        if (set.size() > 0) return set.get(0); else return null;
    }
