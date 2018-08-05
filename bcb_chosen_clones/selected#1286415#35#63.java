    public static StreamSource getSourceNodeInstance(String sourceName, String name, List attributes, Map conditions) {
        SAXBuilder builder = new SAXBuilder();
        try {
            InputStream is = QueryNetworkManager.config.getClass().getClassLoader().getResourceAsStream(SOURCES_FILENAME);
            Document doc = builder.build(is);
            List sources = doc.getRootElement().getChildren();
            boolean found = false;
            for (int i = 0; i < sources.size() && !found; i++) {
                Element source = (Element) sources.get(i);
                if (source.getChild("name").getTextTrim().equals(sourceName)) {
                    found = true;
                    String klazz = source.getChild("class").getTextTrim();
                    Map<String, String> options = new HashMap<String, String>();
                    Element opts;
                    if ((opts = source.getChild("options")) != null) {
                        for (Object o : opts.getChildren()) {
                            Element el = (Element) o;
                            options.put(el.getName(), el.getTextTrim());
                        }
                    }
                    Constructor c = Class.forName(klazz).getConstructor(String.class, Map.class, List.class, Map.class);
                    return (StreamSource) c.newInstance(name, options, attributes, conditions);
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error getting stream source: ", ex);
        }
        return null;
    }
