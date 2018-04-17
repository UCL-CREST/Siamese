    public final RemoteFacade instantiate() throws IdealizeConfigurationException, IdealizeUnavailableResourceException, RemoteException {
        IdealizeClassLoader loader = new IdealizeClassLoader();
        InstantiatorWorker worker = null;
        try {
            worker = (InstantiatorWorker) loader.loadClass(classes.getProperty(this.KEY_INSTANTIATOR)).getConstructor().newInstance();
        } catch (IllegalArgumentException e) {
            throw new IdealizeConfigurationException("Instantiator.instantiate: Could not load instantiator worker: " + classes.getProperty(this.KEY_INSTANTIATOR), e);
        } catch (SecurityException e) {
            throw new IdealizeConfigurationException("Instantiator.instantiate: Could not load instantiator worker: " + classes.getProperty(this.KEY_INSTANTIATOR), e);
        } catch (InstantiationException e) {
            throw new IdealizeConfigurationException("Instantiator.instantiate: Could not load instantiator worker: " + classes.getProperty(this.KEY_INSTANTIATOR), e);
        } catch (IllegalAccessException e) {
            throw new IdealizeConfigurationException("Instantiator.instantiate: Could not load instantiator worker: " + classes.getProperty(this.KEY_INSTANTIATOR), e);
        } catch (InvocationTargetException e) {
            throw new IdealizeConfigurationException("Instantiator.instantiate: Could not load instantiator worker: " + classes.getProperty(this.KEY_INSTANTIATOR), e);
        } catch (ClassNotFoundException e) {
            throw new IdealizeConfigurationException("Instantiator.instantiate: Could not load instantiator worker: " + classes.getProperty(this.KEY_INSTANTIATOR), e);
        } catch (NoSuchMethodException e) {
            throw new IdealizeConfigurationException("Instantiator.instantiate: Could not load instantiator worker: " + classes.getProperty(this.KEY_INSTANTIATOR), e);
        } catch (NullPointerException e) {
            throw new IdealizeConfigurationException("Instantiator.instantiate: Could not load instantiator worker: " + classes.getProperty(this.KEY_INSTANTIATOR), e);
        }
        return worker.doWork(classes, loader);
    }
