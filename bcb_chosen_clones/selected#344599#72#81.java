    public String getTemplateString(String templateFilename) {
        InputStream is = servletContext.getResourceAsStream("/resources/" + templateFilename);
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(is, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
