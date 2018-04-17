    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        boolean zipped = true;
        String fileName = request.getRequestURI();
        int s = fileName.indexOf('/', 1);
        if (s != -1) fileName = fileName.substring(s + 1);
        fileName = this.getServletContext().getRealPath((fileName.indexOf("extjs") == 0) ? "js/" + fileName : fileName);
        File jsFile = new File(fileName);
        if (!jsFile.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        long modifiedValue = jsFile.lastModified();
        Date modified = new Date(modifiedValue);
        long contentLength = jsFile.length();
        String etag = "W/\"" + contentLength + "-" + Long.toString(modifiedValue) + "\"";
        response.setHeader("Etag", etag);
        String ifNoneMatch = request.getHeader("If-None-Match");
        if (ifNoneMatch != null) {
            if (ifNoneMatch.equals(etag)) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }
            jsCache.remove(ifNoneMatch);
        }
        Date lastAccessDate = null;
        String lastAccessDateStr = request.getHeader("If-Modified-Since");
        if (lastAccessDateStr != null) lastAccessDate = new Date(lastAccessDateStr);
        try {
            if (lastAccessDate != null) {
                if (!modified.after(lastAccessDate)) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
            }
            if (fileName.indexOf(".css") > 0) {
                response.setContentType("text/css");
            } else if (fileName.indexOf(".js") > 0) {
                response.setContentType("text/javascript");
            } else if (fileName.indexOf(".jpg") > 0) {
                response.setContentType("image/jpeg");
                zipped = false;
            } else if (fileName.indexOf(".gif") > 0) {
                response.setContentType("image/gif");
                zipped = false;
            } else {
                zipped = false;
            }
            response.setHeader("Last-Modified", modified.toString());
            byte[] content = jsCache.get(etag);
            if (content != null) {
                response.setHeader("Content-Length", Integer.toString(content.length));
                response.getOutputStream().write(content);
                return;
            }
            ByteArrayOutputStream b = new ByteArrayOutputStream((int) (contentLength / 3));
            OutputStream out = null;
            if (zipped) {
                response.setHeader("Content-Encoding", "gzip");
                out = new GZIPOutputStream(b);
            } else {
                out = new BufferedOutputStream(b);
            }
            InputStream in = new FileInputStream(jsFile);
            byte[] buffer = new byte[16384];
            int bufferFill = in.read(buffer);
            while (bufferFill != -1) {
                out.write(buffer, 0, bufferFill);
                bufferFill = in.read(buffer);
            }
            out.close();
            in.close();
            content = b.toByteArray();
            response.setHeader("Content-Length", Integer.toString(content.length));
            response.getOutputStream().write(content);
            jsCache.put(etag, content);
        } catch (Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
    }
