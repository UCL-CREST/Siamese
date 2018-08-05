    @Override
    public DSTDate buildObject(String namespaceURI, String localName, String namespacePrefix) {
        try {
            Class<?> clazz = Class.forName("org.openliberty.xmltooling.pp." + localName);
            Constructor<?> constructor = clazz.getConstructor(new Class[] { String.class, String.class, String.class });
            return (DSTDate) constructor.newInstance(new Object[] { namespaceURI, localName, namespacePrefix });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new DSTDate(namespaceURI, localName, namespacePrefix);
    }
