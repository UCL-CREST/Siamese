    protected String getLibJSCode() throws IOException {
        if (cachedLibJSCode == null) {
            InputStream is = getClass().getResourceAsStream(JS_LIB_FILE);
            StringWriter output = new StringWriter();
            IOUtils.copy(is, output);
            cachedLibJSCode = output.toString();
        }
        return cachedLibJSCode;
    }
