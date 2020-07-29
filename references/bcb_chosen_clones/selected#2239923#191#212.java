    @SuppressWarnings("unchecked")
    private final BaseStorable loadBaseStorable(String className, IdealizeClassLoader loader) throws IdealizeConfigurationException {
        BaseStorable storable = null;
        try {
            storable = (BaseStorable) loader.loadClass(className).getConstructor().newInstance();
        } catch (IllegalArgumentException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadBaseStorable: Could not load storable: " + className, e);
        } catch (SecurityException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadBaseStorable: Could not load storable: " + className, e);
        } catch (InstantiationException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadBaseStorable: Could not load storable: " + className, e);
        } catch (IllegalAccessException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadBaseStorable: Could not load storable: " + className, e);
        } catch (InvocationTargetException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadBaseStorable: Could not load storable: " + className, e);
        } catch (NoSuchMethodException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadBaseStorable: Could not load storable: " + className, e);
        } catch (ClassNotFoundException e) {
            throw new IdealizeConfigurationException(this.getClass().getCanonicalName() + ".loadBaseStorable: Could not load storable: " + className, e);
        }
        return storable;
    }
