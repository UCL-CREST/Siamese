    public FutureCostEstimator getCostCalculator(PhramerConfig config, Instrument instrument) throws PhramerException {
        if (config.futureCostCalculatorClass == null) {
            if (config.compatibilityLevel >= 1000200) return new PharaohDFutureCostCalculator(config, instrument);
            return new PharaohFutureCostCalculator(config, instrument);
        }
        Object[] args = { config, instrument };
        try {
            return (FutureCostEstimator) Class.forName(config.futureCostCalculatorClass).getConstructors()[0].newInstance(args);
        } catch (IllegalAccessException e) {
            throw new PhramerException(e);
        } catch (IllegalArgumentException e) {
            throw new PhramerException(e);
        } catch (ClassNotFoundException e) {
            throw new PhramerException(e);
        } catch (InvocationTargetException e) {
            throw new PhramerException(e);
        } catch (InstantiationException e) {
            throw new PhramerException(e);
        } catch (SecurityException e) {
            throw new PhramerException(e);
        }
    }
