    @SuppressWarnings(value = "unchecked")
    public static BarSymbol createNewBar(BarType barType, JScoreConfig config) throws InternalGuiException {
        BarSymbol symbol = null;
        try {
            Class symbolClass = bars.get(barType);
            if (symbolClass == null) {
                throw new InternalGuiException("Cannot instantiate bar type " + barType);
            }
            Constructor<BarSymbol> symConstructor = symbolClass.getConstructor(JScoreConfig.class);
            symbol = symConstructor.newInstance(config);
        } catch (InstantiationException ex) {
            throw new InternalGuiException("Cannot instantiate bar type " + barType, ex);
        } catch (IllegalAccessException ex) {
            throw new InternalGuiException("Cannot instantiate bar type " + barType, ex);
        } catch (IllegalArgumentException ex) {
            throw new InternalGuiException("Cannot instantiate bar type " + barType, ex);
        } catch (InvocationTargetException ex) {
            throw new InternalGuiException("Cannot instantiate bar type " + barType, ex);
        } catch (NoSuchMethodException ex) {
            throw new InternalGuiException("Cannot instantiate bar type " + barType, ex);
        } catch (SecurityException ex) {
            throw new InternalGuiException("Cannot instantiate bar type " + barType, ex);
        }
        return symbol;
    }
