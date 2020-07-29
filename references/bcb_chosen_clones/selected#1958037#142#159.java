    public void processExplicitSchemaAndWSDL(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HashMap services = configContext.getAxisConfiguration().getServices();
        String filePart = req.getRequestURL().toString();
        String schema = filePart.substring(filePart.lastIndexOf("/") + 1, filePart.length());
        if ((services != null) && !services.isEmpty()) {
            Iterator i = services.values().iterator();
            while (i.hasNext()) {
                AxisService service = (AxisService) i.next();
                InputStream stream = service.getClassLoader().getResourceAsStream("META-INF/" + schema);
                if (stream != null) {
                    OutputStream out = res.getOutputStream();
                    res.setContentType("text/xml");
                    IOUtils.copy(stream, out, true);
                    return;
                }
            }
        }
    }
