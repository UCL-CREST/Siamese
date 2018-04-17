    public Object load(Class[] classes, Object[] args) {
        try {
            Class theClass = Class.forName(className);
            if (classes == null) {
                classes = new Class[0];
                args = new Object[0];
            }
            Constructor constructor = theClass.getConstructor(classes);
            return constructor.newInstance(args);
        } catch (ClassNotFoundException e) {
            logger.log(DAExceptionCodes.MUST_EXIST, this, "load", null, e);
            throw new DASystemException(DAExceptionCodes.MUST_EXIST, new String[] { "class", className });
        } catch (InstantiationException e) {
            logger.log(DAExceptionCodes.MUST_EXIST, this, "load", null, e);
            throw new DASystemException(DAExceptionCodes.MUST_EXIST, new String[] { "class", className });
        } catch (IllegalAccessException e) {
            logger.log(DAExceptionCodes.MUST_EXIST, this, "load", null, e);
            throw new DASystemException(DAExceptionCodes.MUST_EXIST, new String[] { "class", className });
        } catch (NoSuchMethodException e) {
            logger.log(DAExceptionCodes.MUST_EXIST, this, "load", null, e);
            throw new DASystemException(DAExceptionCodes.MUST_EXIST, new String[] { "constructor for class", className });
        } catch (InvocationTargetException e) {
            logger.log(DAExceptionCodes.INVOCATION_ERROR, this, "load", null, e);
            throw new DASystemException(DAExceptionCodes.INVOCATION_ERROR, new String[] { "constructor", className });
        }
    }
