    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentId = req.getParameter(CONTENT_ID);
        String contentType = req.getParameter(CONTENT_TYPE);
        if (contentId == null || contentType == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Content id or content type not specified");
            return;
        }
        try {
            switch(ContentType.valueOf(contentType)) {
                case IMAGE:
                    resp.setContentType("image/jpeg");
                    break;
                case AUDIO:
                    resp.setContentType("audio/mp3");
                    break;
                case VIDEO:
                    resp.setContentType("video/mpeg");
                    break;
                default:
                    throw new IllegalStateException("Invalid content type specified");
            }
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid content type specified");
            return;
        }
        String baseUrl = this.getServletContext().getInitParameter(BASE_URL);
        URL url = new URL(baseUrl + "/" + contentType.toLowerCase() + "/" + contentId);
        URLConnection conn = url.openConnection();
        resp.setContentLength(conn.getContentLength());
        IOUtils.copy(conn.getInputStream(), resp.getOutputStream());
    }
