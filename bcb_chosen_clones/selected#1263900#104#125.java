    private static boolean renderStaticResource(final String requestedResource, HttpServletResponse servletResponse) throws IOException {
        boolean successfull = true;
        String fileName = PathTool.getFSPathOfResource(requestedResource);
        File file = new File(fileName);
        if (!file.exists()) {
            logger.error("Static resource not found: " + fileName);
            return false;
        }
        if (fileName.endsWith("xml") || fileName.endsWith("asp")) servletResponse.setContentType("text/xml"); else if (fileName.endsWith("css")) servletResponse.setContentType("text/css"); else if (fileName.endsWith("js")) servletResponse.setContentType("text/javascript");
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            IOUtils.copy(in, servletResponse.getOutputStream());
            logger.debug("Static resource rendered: ".concat(fileName));
        } catch (FileNotFoundException e) {
            logger.error("Static resource not found: " + fileName);
            successfull = false;
        } finally {
            IOUtils.closeQuietly(in);
        }
        return successfull;
    }
