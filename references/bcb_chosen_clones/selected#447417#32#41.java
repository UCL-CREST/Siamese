    public static void downloadFile(HttpServletResponse response, String file) throws IOException {
        response.setContentType(FileUtils.getContentType(file));
        response.setContentLength(FileUtils.getContentLength(file));
        response.setHeader("Content-type", ResponseUtils.DOWNLOAD_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + FileUtils.getFileName(file) + "\"");
        response.setHeader("Content-Length", Integer.toString(FileUtils.getContentLength(file)));
        InputStream input = new FileInputStream(file);
        OutputStream output = response.getOutputStream();
        IOUtils.copy(input, output, true);
    }
