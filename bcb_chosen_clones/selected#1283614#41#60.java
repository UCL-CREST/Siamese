    public void doRender() throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            logger.error("Static resource not found: " + fileName);
            isNotFound = true;
            return;
        }
        if (fileName.endsWith("xml") || fileName.endsWith("asp")) servletResponse.setContentType("text/xml"); else if (fileName.endsWith("css")) servletResponse.setContentType("text/css"); else if (fileName.endsWith("js")) servletResponse.setContentType("text/javascript");
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            IOUtils.copy(in, servletResponse.getOutputStream());
            logger.debug("Static resource rendered: ".concat(fileName));
        } catch (FileNotFoundException e) {
            logger.error("Static resource not found: " + fileName);
            isNotFound = true;
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
