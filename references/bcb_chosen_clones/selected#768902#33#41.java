    public Object createXPathEvaluator(Document doc) {
        try {
            Class xpathClass = Class.forName("org.apache.xpath.domapi.XPathEvaluatorImpl");
            Constructor constructor = xpathClass.getConstructor(new Class[] { Document.class });
            return constructor.newInstance(new Object[] { doc });
        } catch (Exception ex) {
        }
        return doc;
    }
