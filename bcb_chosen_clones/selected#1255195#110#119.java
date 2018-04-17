    private <T extends AbstractOperatorAlgorithm> T createOperatorInstance(Class<T> clz) throws OperatorLoadingException {
        T operator = null;
        try {
            Constructor<T> constructor = clz.getConstructor();
            operator = constructor.newInstance();
        } catch (Exception e) {
            throw new OperatorLoadingException(e);
        }
        return operator;
    }
