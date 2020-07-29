    @Override
    public String fetchElectronicEdition(Publication pub) {
        final String url = pub.getEe();
        HttpMethod method = null;
        String responseBody = "";
        method = new GetMethod(url);
        method.setFollowRedirects(true);
        try {
            if (StringUtils.isNotBlank(method.getURI().getScheme())) {
                InputStream is = null;
                StringWriter writer = new StringWriter();
                try {
                    client.executeMethod(method);
                    Header contentType = method.getResponseHeader("Content-Type");
                    if (contentType != null && StringUtils.isNotBlank(contentType.getValue()) && contentType.getValue().indexOf("text/html") >= 0) {
                        is = method.getResponseBodyAsStream();
                        IOUtils.copy(is, writer);
                        responseBody = writer.toString();
                    } else {
                        logger.info("ignoring non-text/html response from page: " + url + " content-type:" + contentType);
                    }
                } catch (HttpException he) {
                    logger.error("Http error connecting to '" + url + "'");
                    logger.error(he.getMessage());
                } catch (IOException ioe) {
                    logger.error("Unable to connect to '" + url + "'");
                } finally {
                    IOUtils.closeQuietly(is);
                    IOUtils.closeQuietly(writer);
                }
            }
        } catch (URIException e) {
            logger.error(e);
        } finally {
            method.releaseConnection();
        }
        return responseBody;
    }
