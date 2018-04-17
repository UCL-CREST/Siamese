    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uuid = req.getParameterValues(Constants.PARAM_UUID)[0];
        String datastream = null;
        if (req.getRequestURI().contains(Constants.SERVLET_DOWNLOAD_FOXML_PREFIX)) {
            resp.addHeader("Content-Disposition", "attachment; ContentType = \"text/xml\"; filename=\"" + uuid + "_server_version.foxml\"");
        } else {
            datastream = req.getParameterValues(Constants.PARAM_DATASTREAM)[0];
            resp.addHeader("Content-Disposition", "attachment; ContentType = \"text/xml\"; filename=\"" + uuid + "_server_version_" + datastream + ".xml\"");
        }
        ServletOutputStream os = resp.getOutputStream();
        if (uuid != null && !"".equals(uuid)) {
            try {
                StringBuffer sb = new StringBuffer();
                if (req.getRequestURI().contains(Constants.SERVLET_DOWNLOAD_FOXML_PREFIX)) {
                    sb.append(config.getFedoraHost()).append("/objects/").append(uuid).append("/objectXML");
                } else if (req.getRequestURI().contains(Constants.SERVLET_DOWNLOAD_DATASTREAMS_PREFIX)) {
                    sb.append(config.getFedoraHost()).append("/objects/").append(uuid).append("/datastreams/").append(datastream).append("/content");
                }
                InputStream is = RESTHelper.get(sb.toString(), config.getFedoraLogin(), config.getFedoraPassword(), false);
                if (is == null) {
                    return;
                }
                try {
                    if (req.getRequestURI().contains(Constants.SERVLET_DOWNLOAD_DATASTREAMS_PREFIX)) {
                        os.write(Constants.XML_HEADER_WITH_BACKSLASHES.getBytes());
                    }
                    IOUtils.copyStreams(is, os);
                } catch (IOException e) {
                    resp.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
                    LOGGER.error("Problem with downloading foxml.", e);
                } finally {
                    os.flush();
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            resp.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
                            LOGGER.error("Problem with downloading foxml.", e);
                        } finally {
                            is = null;
                        }
                    }
                }
            } catch (IOException e) {
                resp.setStatus(HttpURLConnection.HTTP_NOT_FOUND);
                LOGGER.error("Problem with downloading foxml.", e);
            } finally {
                os.flush();
            }
        }
    }
