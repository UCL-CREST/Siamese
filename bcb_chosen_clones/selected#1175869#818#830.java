    private static Exception createServiceException(String message, Class exceptionclass, Object bean, Class beanFormalType, MarshalServiceRuntimeDescription marshalDesc, boolean isLegacyException) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        if (log.isDebugEnabled()) {
            log.debug("Constructing JAX-WS Exception:" + exceptionclass);
        }
        Exception exception = null;
        if (isLegacyException) {
            exception = LegacyExceptionUtil.createFaultException(exceptionclass, bean, marshalDesc);
        } else {
            Constructor constructor = exceptionclass.getConstructor(new Class[] { String.class, beanFormalType });
            exception = (Exception) constructor.newInstance(new Object[] { message, bean });
        }
        return exception;
    }
