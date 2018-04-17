    @SuppressWarnings("unchecked")
    private static Manager create(String mgrProviderClass) throws InitializationException {
        instance = null;
        try {
            @SuppressWarnings("rawtypes") Class providerClass = Class.forName(mgrProviderClass, true, Thread.currentThread().getContextClassLoader());
            Constructor<Manager> constructor = providerClass.getConstructor();
            instance = constructor.newInstance();
        } catch (InstantiationException e) {
            falscheImplementierung(mgrProviderClass, e);
        } catch (IllegalAccessException e) {
            falscheImplementierung(mgrProviderClass, e);
        } catch (NoSuchMethodException e) {
            falscheImplementierung(mgrProviderClass, e);
        } catch (ClassNotFoundException e) {
            String msg = "Provider-Klasse (" + mgrProviderClass + ") wurde nicht gefunden!";
            throw new InitializationException(msg, e);
        } catch (InvocationTargetException e) {
            String msg = "Konstruktor der Provider-Klasse (" + mgrProviderClass + ") erzeugte eine Exception: " + e.getMessage();
            logger.throwing(Manager.class.getName(), "create", e);
            throw new InitializationException(msg, e);
        }
        return instance;
    }
