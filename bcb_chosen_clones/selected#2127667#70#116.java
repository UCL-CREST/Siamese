    @Override
    public Box createBox(String type, byte[] userType, String parent) {
        FourCcToBox fourCcToBox = new FourCcToBox(type, userType, parent).invoke();
        String[] param = fourCcToBox.getParam();
        String clazzName = fourCcToBox.getClazzName();
        try {
            if (param[0].trim().length() == 0) {
                param = new String[] {};
            }
            Class clazz = Class.forName(clazzName);
            Class[] constructorArgsClazz = new Class[param.length];
            Object[] constructorArgs = new Object[param.length];
            for (int i = 0; i < param.length; i++) {
                if ("userType".equals(param[i])) {
                    constructorArgs[i] = userType;
                    constructorArgsClazz[i] = byte[].class;
                } else if ("type".equals(param[i])) {
                    constructorArgs[i] = type;
                    constructorArgsClazz[i] = String.class;
                } else if ("parent".equals(param[i])) {
                    constructorArgs[i] = parent;
                    constructorArgsClazz[i] = String.class;
                } else {
                    throw new InternalError("No such param: " + param[i]);
                }
            }
            Constructor<AbstractBox> constructorObject;
            try {
                if (param.length > 0) {
                    constructorObject = clazz.getConstructor(constructorArgsClazz);
                } else {
                    constructorObject = clazz.getConstructor();
                }
                return constructorObject.newInstance(constructorArgs);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
