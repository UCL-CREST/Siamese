    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentType = req.getParameter("type");
        String arg = req.getParameter("file");
        if (arg == null) {
            resp.sendError(404, "Missing File Arg");
            return;
        }
        File f = new File(arg);
        if (!f.exists()) {
            resp.sendError(404, "Missing File: " + f);
            return;
        }
        if (contentType != null) {
            resp.setContentType(contentType);
        }
        log.debug("Requested File: " + f + " as type: " + contentType);
        resp.setContentLength((int) f.length());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            OutputStream os = resp.getOutputStream();
            IOUtils.copyLarge(fis, os);
            os.flush();
            fis.close();
        } catch (Throwable e) {
            log.error("Failed to send file: " + f);
            resp.sendError(500, "Failed to get file " + f);
        } finally {
            IOUtils.closeQuietly(fis);
        }
    }
