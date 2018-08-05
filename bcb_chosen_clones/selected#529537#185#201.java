    protected IAeBpelExpressionLanguageFactory createFactory(Map aMap) throws AeException {
        if (AeUtil.isNullOrEmpty(aMap)) {
            return null;
        }
        String className = (String) aMap.get(IAeEngineConfiguration.CLASS_ENTRY);
        if (className == null) {
            throw new AeException(AeMessages.getString("AeExpressionLanguageFactory.NO_CLASS_SPECIFIED_FOR_FACTORY"));
        }
        try {
            Class clazz = Class.forName(className);
            Constructor cons = clazz.getConstructor(new Class[] { Map.class, ClassLoader.class });
            return (IAeBpelExpressionLanguageFactory) cons.newInstance(new Object[] { aMap, getFactoryClassloader() });
        } catch (Exception e) {
            AeException.logError(e, AeMessages.format("AeExpressionLanguageFactory.ERROR_INSTANTIATING_FACTORY", className));
            throw new AeException(AeMessages.format("AeExpressionLanguageFactory.ERROR_INSTANTIATING_FACTORY", className));
        }
    }
