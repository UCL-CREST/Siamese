    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        res.setHeader("X-Generator", "VisualMon");
        String path = req.getPathInfo();
        if (null == path || "".equals(path)) res.sendRedirect(req.getServletPath() + "/"); else if ("/chart".equals(path)) {
            try {
                res.setHeader("Cache-Control", "private,no-cache,no-store,must-revalidate");
                res.addHeader("Cache-Control", "post-check=0,pre-check=0");
                res.setHeader("Expires", "Sat, 26 Jul 1997 05:00:00 GMT");
                res.setHeader("Pragma", "no-cache");
                res.setDateHeader("Expires", 0);
                renderChart(req, res);
            } catch (InterruptedException e) {
                log.info("Chart generation was interrupted", e);
                Thread.currentThread().interrupt();
            }
        } else if (path.startsWith("/log_")) {
            String name = path.substring(5);
            LogProvider provider = null;
            for (LogProvider prov : cfg.getLogProviders()) {
                if (name.equals(prov.getName())) {
                    provider = prov;
                    break;
                }
            }
            if (null == provider) {
                log.error("Log provider with name \"{}\" not found", name);
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
            } else {
                render(res, provider.getLog(filter.getLocale()));
            }
        } else if ("/".equals(path)) {
            List<LogEntry> logs = new ArrayList<LogEntry>();
            for (LogProvider provider : cfg.getLogProviders()) logs.add(new LogEntry(provider.getName(), provider.getTitle(filter.getLocale())));
            render(res, new ProbeDataList(filter.getSnapshot(), filter.getAlerts(), logs, ResourceBundle.getBundle("de.frostcode.visualmon.stats", filter.getLocale()).getString("category.empty"), cfg.getDashboardTitle().get(filter.getLocale())));
        } else {
            URL url = Thread.currentThread().getContextClassLoader().getResource(getClass().getPackage().getName().replace('.', '/') + req.getPathInfo());
            if (null == url) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            res.setDateHeader("Last-Modified", new File(url.getFile()).lastModified());
            res.setDateHeader("Expires", new Date().getTime() + YEAR_IN_SECONDS * 1000L);
            res.setHeader("Cache-Control", "max-age=" + YEAR_IN_SECONDS);
            URLConnection conn = url.openConnection();
            String resourcePath = url.getPath();
            String contentType = conn.getContentType();
            if (resourcePath.endsWith(".xsl")) {
                contentType = "text/xml";
                res.setCharacterEncoding(ENCODING);
            }
            if (contentType == null || "content/unknown".equals(contentType)) {
                if (resourcePath.endsWith(".css")) contentType = "text/css"; else contentType = getServletContext().getMimeType(resourcePath);
            }
            res.setContentType(contentType);
            res.setContentLength(conn.getContentLength());
            OutputStream out = res.getOutputStream();
            IOUtils.copy(conn.getInputStream(), out);
            IOUtils.closeQuietly(conn.getInputStream());
            IOUtils.closeQuietly(out);
        }
    }
