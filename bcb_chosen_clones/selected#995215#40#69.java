    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (beforeServingFile(req, resp)) {
            String pathInfo = req.getPathInfo();
            Validate.notNull(pathInfo, "the path info is null -> the sevlet should be mapped with /<mapping>/*");
            String resurouce = pathInfo.substring(1);
            if (log.isDebugEnabled()) {
                log.debug("resource to expose: " + resurouce);
            }
            String extension = resurouce.substring(resurouce.lastIndexOf('.') + 1);
            MimeType mimeType = MimeTypeRegistry.getByExtension(extension);
            Validate.notNull(mimeType, "no mimetype found for extension: " + extension);
            if (log.isDebugEnabled()) {
                log.debug("the mime type to set: " + mimeType.getMimeType());
            }
            File f = new File(mappedFolder, resurouce);
            Validate.isTrue(f.exists(), "file: " + f + " does not exist");
            Validate.isTrue(f.canRead(), "can not read the file: " + f);
            if (log.isDebugEnabled()) {
                log.debug("exposing the file: " + f);
            }
            resp.setContentType(mimeType.getMimeType());
            FileInputStream fis = new FileInputStream(f);
            ServletOutputStream os = resp.getOutputStream();
            IOUtils.copy(fis, os);
            os.flush();
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(os);
        }
    }
