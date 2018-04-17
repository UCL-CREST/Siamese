    private void output(HttpServletResponse resp, InputStream is, long length, String fileName) throws Exception {
        resp.reset();
        String mimeType = "image/jpeg";
        resp.setContentType(mimeType);
        resp.setContentLength((int) length);
        resp.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
        resp.setHeader("Cache-Control", "must-revalidate");
        ServletOutputStream sout = resp.getOutputStream();
        IOUtils.copy(is, sout);
        sout.flush();
        resp.flushBuffer();
    }
