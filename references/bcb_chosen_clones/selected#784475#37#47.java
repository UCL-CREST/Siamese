    private void download(Downloadable downloadable, HttpServletResponse response) throws Exception {
        String contentType = downloadable.getContentType();
        try {
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment;" + " filename=" + new String(downloadable.getName().getBytes(), "ISO-8859-1"));
            OutputStream os = response.getOutputStream();
            IOUtils.copy(downloadable.getInputStream(), os);
            os.flush();
        } catch (IOException e) {
        }
    }
