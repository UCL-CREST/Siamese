    public HomeWorksDevice getHomeWorksDevice(LutronHomeWorksAddress address, Class<? extends HomeWorksDevice> deviceClass) throws LutronHomeWorksDeviceException {
        HomeWorksDevice device = deviceCache.get(address);
        if (device == null) {
            try {
                Constructor<? extends HomeWorksDevice> constructor = deviceClass.getConstructor(LutronHomeWorksGateway.class, LutronHomeWorksAddress.class);
                device = constructor.newInstance(this, address);
            } catch (SecurityException e) {
                throw new LutronHomeWorksDeviceException("Impossible to create device instance", address, deviceClass, e);
            } catch (NoSuchMethodException e) {
                throw new LutronHomeWorksDeviceException("Impossible to create device instance", address, deviceClass, e);
            } catch (IllegalArgumentException e) {
                throw new LutronHomeWorksDeviceException("Impossible to create device instance", address, deviceClass, e);
            } catch (InstantiationException e) {
                throw new LutronHomeWorksDeviceException("Impossible to create device instance", address, deviceClass, e);
            } catch (IllegalAccessException e) {
                throw new LutronHomeWorksDeviceException("Impossible to create device instance", address, deviceClass, e);
            } catch (InvocationTargetException e) {
                throw new LutronHomeWorksDeviceException("Impossible to create device instance", address, deviceClass, e);
            }
        }
        if (!(deviceClass.isInstance(device))) {
            throw new LutronHomeWorksDeviceException("Invalid device type found at given address", address, deviceClass, null);
        }
        deviceCache.put(address, device);
        return device;
    }
