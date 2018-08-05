    private void proxyMediaServlet(String url, HttpServletRequest req, HttpServletResponse resp) {
        try {
            URL u = new URL("http://mediaserver:8080/sagex" + url);
            log.debug("Proxy Media: " + u.toString());
            URLConnection c = u.openConnection();
            c.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.1) Gecko/2008072820 Firefox/3.0.1");
            OutputStream os = resp.getOutputStream();
            IOUtils.copy(c.getInputStream(), os);
            os.flush();
            resp.flushBuffer();
        } catch (Throwable t) {
            log.error("Failed to get url: " + url, t);
            try {
                resp.sendError(500, t.getMessage());
            } catch (IOException e) {
            }
        }
    }
