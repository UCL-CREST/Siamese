    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String arg = req.getParameter("file");
        if (arg == null) {
            resp.sendError(404, "Missing 'file' Arg");
            return;
        }
        int mfid = NumberUtils.toInt(arg);
        Object sageFile = MediaFileAPI.GetMediaFileForID(mfid);
        if (sageFile == null) {
            resp.sendError(404, "Sage File not found " + mfid);
            return;
        }
        int seconds = NumberUtils.toInt(req.getParameter("ss"), -1);
        long offset = NumberUtils.toLong(req.getParameter("sb"), -1);
        if (seconds < 0 && offset < 0) {
            resp.sendError(501, "Missing 'ss' or 'sb' args");
            return;
        }
        int width = NumberUtils.toInt(req.getParameter("w"), 320);
        int height = NumberUtils.toInt(req.getParameter("h"), 320);
        File dir = new File(Phoenix.getInstance().getUserCacheDir(), "videothumb/" + mfid);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String prefix = "";
        if (offset > 0) {
            prefix = "O" + offset;
        } else {
            prefix = "S" + seconds;
        }
        File f = new File(dir, prefix + "_" + width + "_" + height + ".jpg").getCanonicalFile();
        if (!f.exists()) {
            try {
                generateThumbnailNew(sageFile, f, seconds, offset, width, height);
            } catch (Exception e) {
                e.printStackTrace();
                resp.sendError(503, "Failed to generate thumbnail\n " + e.getMessage());
                return;
            }
        }
        if (!f.exists()) {
            resp.sendError(404, "Missing File: " + f);
            return;
        }
        resp.setContentType("image/jpeg");
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
