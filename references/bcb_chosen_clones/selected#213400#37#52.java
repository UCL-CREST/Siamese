    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameterValues(Constants.PARAM_UUID)[0];
        String datastream = null;
        if (req.getRequestURI().contains(Constants.SERVLET_DOWNLOAD_FOXML_PREFIX)) {
            resp.addHeader("Content-Disposition", "attachment; ContentType = \"text/xml\"; filename=\"" + uuid + "_local_version.foxml\"");
        } else {
            datastream = req.getParameterValues(Constants.PARAM_DATASTREAM)[0];
            resp.addHeader("Content-Disposition", "attachment; ContentType = \"text/xml\"; filename=\"" + uuid + "_local_version_" + datastream + ".xml\"");
        }
        String xmlContent = URLDecoder.decode(req.getParameterValues(Constants.PARAM_CONTENT)[0], "UTF-8");
        InputStream is = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
        ServletOutputStream os = resp.getOutputStream();
        IOUtils.copyStreams(is, os);
        os.flush();
    }
