    private void createPropertyName(String objectID, String value, String propertyName, Long userID) throws JspTagException {
        rObject object = new rObject(new Long(objectID), userID);
        ClassProperty classProperty = new ClassProperty(propertyName, object.getClassName());
        String newValue = value;
        if (classProperty.getName().equals("Password")) {
            try {
                MessageDigest crypt = MessageDigest.getInstance("MD5");
                crypt.update(value.getBytes());
                byte digest[] = crypt.digest();
                StringBuffer hexString = new StringBuffer();
                for (int i = 0; i < digest.length; i++) {
                    hexString.append(hexDigit(digest[i]));
                }
                newValue = hexString.toString();
                crypt.reset();
            } catch (NoSuchAlgorithmException e) {
                System.err.println("jspShop: Could not get instance of MD5 algorithm. Please fix this!" + e.getMessage());
                e.printStackTrace();
                throw new JspTagException("Error crypting password!: " + e.getMessage());
            }
        }
        Properties properties = new Properties(new Long(objectID), userID);
        try {
            Property property = properties.create(classProperty.getID(), newValue);
            pageContext.setAttribute(getId(), property);
        } catch (CreateException e) {
            throw new JspTagException("Could not create PropertyValue, CreateException: " + e.getMessage());
        }
    }
