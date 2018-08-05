    public Object get(String sType) {
        Class<?> cTranslator;
        Translator oTranslator = null;
        try {
            cTranslator = (Class<?>) this.hmTranslators.get(sType);
            Constructor<?> oConstructor = cTranslator.getConstructor(String.class);
            oTranslator = (Translator) oConstructor.newInstance(sType);
        } catch (NullPointerException oException) {
            throw new SystemException(ErrorCode.TRANSLATORS_FACTORY, sType, oException);
        } catch (Exception oException) {
            throw new SystemException(ErrorCode.TRANSLATORS_FACTORY, sType, oException);
        }
        return oTranslator;
    }
