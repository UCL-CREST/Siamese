    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("i") != null) {
            String img = req.getParameter("i");
            if (img == null) {
                resp.sendError(404, "Image was null");
                return;
            }
            File f = null;
            if (img.startsWith("file")) {
                try {
                    f = new File(new URI(img));
                } catch (URISyntaxException e) {
                    resp.sendError(500, e.getMessage());
                    return;
                }
            } else {
                f = new File(img);
            }
            if (f.exists()) {
                f = f.getCanonicalFile();
                if (f.getName().endsWith(".jpg") || f.getName().endsWith(".png")) {
                    resp.setContentType("image/png");
                    FileInputStream fis = null;
                    OutputStream os = resp.getOutputStream();
                    try {
                        fis = new FileInputStream(f);
                        IOUtils.copy(fis, os);
                    } finally {
                        os.flush();
                        if (fis != null) fis.close();
                    }
                }
            }
            return;
        }
        String mediaUrl = "/media" + req.getPathInfo();
        String parts[] = mediaUrl.split("/");
        mediaHandler.handleRequest(parts, req, resp);
    }
