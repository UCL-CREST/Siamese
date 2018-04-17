    protected void loadText(final Element elem) {
        final String urlAttr = elem.getAttributeValue("url");
        if (urlAttr != null) {
            String result = "";
            try {
                final URL url = new URL(DatabaseViewerManager.baseURL, urlAttr);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = reader.readLine();
                while (line != null) {
                    result += line + "\n";
                    line = reader.readLine();
                }
                elem.addContent(result);
                elem.removeAttribute("url");
            } catch (final Exception e) {
                throw new RuntimeException("Could not insert text template for database viewer from file", e);
            }
        }
    }
