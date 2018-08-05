    public Attribute getInstance(String attributeClass, String key) throws InvalidClassException {
        if (!(attributeClass.contains(PACKAGE_CONSTRAINT))) {
            throw new InvalidClassException();
        }
        Attribute a;
        try {
            Class<Attribute> c = (Class<Attribute>) Class.forName(attributeClass);
            Object[] params = new Object[1];
            params[0] = key;
            try {
                Constructor con = c.getConstructor(proto);
                a = (Attribute) con.newInstance(params);
            } catch (NoSuchMethodException e) {
                a = c.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return a;
    }
