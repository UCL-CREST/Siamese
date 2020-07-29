    public static Object invokeInitializer(Class cls, int methodID, int argAddress, boolean isJvalue, boolean isDotDotStyle) throws Exception {
        VM_Method mth = VM_MethodDictionary.getValue(methodID);
        VM_Type[] argTypes = mth.getParameterTypes();
        Class[] argClasses = new Class[argTypes.length];
        for (int i = 0; i < argClasses.length; i++) {
            argClasses[i] = argTypes[i].getClassForType();
        }
        Constructor constMethod = cls.getConstructor(argClasses);
        if (constMethod == null) throw new Exception("Constructor not found");
        int varargAddress;
        if (isDotDotStyle) varargAddress = getVarArgAddress(false); else varargAddress = argAddress;
        Object argObjs[];
        if (isJvalue) argObjs = packageParameterFromJValue(mth, argAddress); else argObjs = packageParameterFromVarArg(mth, varargAddress);
        Object newobj = constMethod.newInstance(argObjs);
        return newobj;
    }
