    private void createProperty(String objectID, String value, String propertyID, Long userID) throws JspTagException {
        ClassProperty classProperty = new ClassProperty(new Long(propertyID));
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
            Property property = properties.create(new Long(propertyID), newValue);
            pageContext.setAttribute(getId(), property);
        } catch (CreateException e) {
            throw new JspTagException("Could not create PropertyValue, CreateException: " + e.getMessage());
        }
    }
