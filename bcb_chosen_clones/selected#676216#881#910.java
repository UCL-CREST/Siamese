    private IGpxCreatable instantiateCreatable(String parms) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        IGpxCreatable object = null;
        String[] parmsArray = parms.split(",");
        String className = parmsArray[0];
        Class objclass = Class.forName(className);
        if (parmsArray.length > 1) {
            Class[] constructTypes = new Class[parmsArray.length - 1];
            Object[] constructVals = new Object[parmsArray.length - 1];
            for (int ii = 1; ii < parmsArray.length; ++ii) {
                String[] parm = parmsArray[ii].split("=");
                String constructType = parm[0];
                String constructVal = parm[1];
                constructTypes[ii - 1] = Class.forName(constructType);
                Class[] valConstructorArgs = new Class[1];
                valConstructorArgs[0] = Class.forName("java.lang.String");
                Constructor objConstructor = constructTypes[ii - 1].getConstructor(valConstructorArgs);
                String[] valStringArr = new String[1];
                valStringArr[0] = constructVal;
                Object obj = objConstructor.newInstance(valStringArr);
                constructVals[ii - 1] = obj;
            }
            Constructor constructor = objclass.getConstructor(constructTypes);
            object = (IGpxCreatable) constructor.newInstance(constructVals);
        } else {
            object = (IGpxCreatable) objclass.newInstance();
            ArgsRequestor argsReq = new ArgsRequestor(mainFrame, className, object);
            object = argsReq.getObject();
        }
        return object;
    }
