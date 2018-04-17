    public void Delete(String itemPK) {
        Poll poll = new Poll();
        Field primaryKeyField = null;
        for (Field field : clazz.getDeclaredFields()) {
            Annotation ann = field.getAnnotation(Id.class);
            if (ann != null) {
                primaryKeyField = field;
                break;
            }
        }
        Method m = null;
        try {
            String methodName = "set" + primaryKeyField.getName().substring(0, 1).toUpperCase() + primaryKeyField.getName().substring(1);
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    Logger.getLogger(Polls.class.getName()).info(methodName);
                    m = method;
                    break;
                }
            }
            Object parameterObject = Conversion.Cast(itemPK, m.getParameterTypes()[0]);
            m.invoke(poll, new Object[] { parameterObject });
        } catch (Exception ex) {
            Logger.getLogger(Polls.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            poll.Delete();
        } catch (Exception ex) {
            Logger.getLogger(Polls.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
