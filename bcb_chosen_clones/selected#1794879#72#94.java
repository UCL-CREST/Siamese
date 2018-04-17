    @Override
    protected void doRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqPath = req.getPathInfo();
        if (reqPath.startsWith("/")) reqPath = reqPath.substring(1);
        ZipEntry entry = zipInfo.get(reqPath);
        if (entry == null) {
            logger.debug(Utils.join("Requested path not found: [", reqPath, "]"));
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        logger.debug(Utils.join("Requested path: [", reqPath, "]"));
        ServletUtils.establishContentType(reqPath, resp);
        InputStream in = null;
        try {
            in = new BufferedInputStream(zipFile.getInputStream(entry));
            IOUtils.copy(in, resp.getOutputStream());
            logger.debug("Rendered: " + reqPath);
        } catch (FileNotFoundException e) {
            logger.error("zipped resource not found: " + reqPath);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
