    private XmlParsedData createXmlParsedData(String filename, String modeName, boolean html) {
        String dataClassName = jEdit.getProperty("xml.xmlparseddata." + modeName);
        if (dataClassName != null) {
            try {
                Class dataClass = Class.forName(dataClassName);
                java.lang.reflect.Constructor con = dataClass.getConstructor(String.class, Boolean.TYPE);
                return (XmlParsedData) con.newInstance(filename, html);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new XmlParsedData(filename, html);
    }
