    public String readTemplateToString(String fileName) {
        URL url = null;
        url = classLoader.getResource(fileName);
        StringBuffer content = new StringBuffer();
        if (url == null) {
            String error = "Template file could not be found: " + fileName;
            throw new RuntimeException(error);
        }
        try {
            BufferedReader breader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String strLine = "";
            while ((strLine = breader.readLine()) != null) {
                content.append(strLine).append("\n");
            }
            breader.close();
        } catch (Exception e) {
            throw new RuntimeException("Problem while loading file: " + fileName);
        }
        return content.toString();
    }
