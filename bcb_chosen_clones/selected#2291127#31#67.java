    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Cache-Control", "max-age=" + Constants.HTTP_CACHE_SECONDS);
        String uuid = req.getRequestURI().substring(req.getRequestURI().indexOf(Constants.SERVLET_THUMBNAIL_PREFIX) + Constants.SERVLET_THUMBNAIL_PREFIX.length() + 1);
        if (uuid != null && !"".equals(uuid)) {
            resp.setContentType("image/jpeg");
            StringBuffer sb = new StringBuffer();
            sb.append(config.getFedoraHost()).append("/objects/").append(uuid).append("/datastreams/IMG_THUMB/content");
            InputStream is = null;
            if (!Constants.MISSING.equals(uuid)) {
                is = RESTHelper.get(sb.toString(), config.getFedoraLogin(), config.getFedoraPassword(), true);
            } else {
                is = new FileInputStream(new File("images/other/file_not_found.png"));
            }
            if (is == null) {
                return;
            }
            ServletOutputStream os = resp.getOutputStream();
            try {
                IOUtils.copyStreams(is, os);
            } catch (IOException e) {
            } finally {
                os.flush();
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                    } finally {
                        is = null;
                    }
                }
            }
            resp.setStatus(200);
        } else {
            resp.setStatus(404);
        }
    }
