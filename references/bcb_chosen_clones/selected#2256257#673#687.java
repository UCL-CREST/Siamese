    public static ResultReader createReader(Class type, String name) throws ConfigurationException {
        if (type == null) {
            throw new NullPointerException();
        }
        if (!ResultReader.class.isAssignableFrom(type)) {
            throw new ConfigurationException(type.getName() + " is not instance of " + ObjectReader.class.getName());
        }
        try {
            Constructor ct = type.getConstructor(new Class[] { String.class });
            return (ResultReader) ct.newInstance(new Object[] { name });
        } catch (Exception ex) {
            SQLManager.log.warn("createReader:" + name, ex);
            throw new ConfigurationException("Can't create [ResultReader] class:" + type.getName() + ".");
        }
    }
