    private Object createElement(Object father, String childName) {
        Object child = null;
        int arrayIndex = 0;
        String methodName = null;
        String setMethodName = null;
        Method method;
        Method setMethod;
        Class[] parameterTypes = new Class[] {};
        Class[] setParameterTypes = new Class[] {};
        Object[] arguments = new Object[] {};
        Object[] setParameters = new Object[] {};
        Object object = null;
        try {
            Class c = father.getClass();
            if (childName.indexOf(LEFT_BRACKET) != -1) {
                arrayIndex = (new Integer(childName.substring(childName.indexOf(LEFT_BRACKET) + 1, childName.indexOf(RIGHT_BRACKET)))).intValue();
                childName = childName.substring(0, childName.indexOf(LEFT_BRACKET));
            }
            methodName = GETTER_PREFIX.concat(childName);
            method = c.getMethod(methodName, parameterTypes);
            if (method.invoke(father, arguments) == null) {
                try {
                    String classPath = method.getReturnType().getCanonicalName();
                    if (method.getReturnType().isArray()) {
                        classPath = classPath.substring(0, classPath.indexOf(LEFT_BRACKET));
                    }
                    Class classDefinition = Class.forName(classPath);
                    if (method.getReturnType().isArray()) {
                        object = Array.newInstance(classDefinition, arrayIndex + 1);
                        Array.set(object, 0, classDefinition.newInstance());
                    } else {
                        object = classDefinition.newInstance();
                    }
                    setParameters = new Object[] { object };
                    setParameterTypes = new Class[] { method.getReturnType() };
                    setMethodName = SETTER_PREFIX.concat(childName);
                    setMethod = c.getMethod(setMethodName, setParameterTypes);
                    setMethod.invoke(father, setParameters);
                } catch (Exception e) {
                    logger.error("IFXMessage|createElement(" + father.toString() + "," + childName + "): " + e.getMessage() + "|");
                }
            }
            if (method.getReturnType().isArray()) {
                Object array = null;
                Object auxArray = null;
                array = method.invoke(father, arguments);
                try {
                    if (Array.getLength(array) < arrayIndex + 1) {
                        auxArray = Array.newInstance(method.getReturnType().getComponentType(), Array.getLength(array));
                        System.arraycopy(array, 0, auxArray, 0, Array.getLength(array));
                        array = Array.newInstance(method.getReturnType().getComponentType(), arrayIndex + 1);
                        System.arraycopy(auxArray, 0, array, 0, Array.getLength(auxArray));
                        Array.set(array, arrayIndex, method.getReturnType().getComponentType().newInstance());
                        object = array;
                        setParameters = new Object[] { object };
                        setParameterTypes = new Class[] { method.getReturnType() };
                        setMethodName = SETTER_PREFIX.concat(childName);
                        setMethod = c.getMethod(setMethodName, setParameterTypes);
                        setMethod.invoke(father, setParameters);
                    } else {
                        if (Array.get(array, arrayIndex) == null) {
                            Array.set(array, arrayIndex, method.getReturnType().getComponentType().newInstance());
                        }
                    }
                } catch (Exception e) {
                    logger.error("IFXMessage|createElement(" + father.toString() + "," + childName + "): " + e.getMessage() + "|");
                }
                child = Array.get(array, arrayIndex);
            } else {
                child = method.invoke(father, arguments);
            }
        } catch (NoSuchMethodException e) {
            child = null;
        } catch (IllegalAccessException e) {
            child = null;
        } catch (InvocationTargetException e) {
            child = null;
        }
        return child;
    }
