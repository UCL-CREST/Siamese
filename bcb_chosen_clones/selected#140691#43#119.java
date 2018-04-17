    @SuppressWarnings("unchecked")
    protected void processDownloadAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        File transformationFile = new File(xslBase, "file-info.xsl");
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.putAll(request.getParameterMap());
        params.put("{" + Definitions.CONFIGURATION_NAMESPACE + "}configuration", configuration);
        params.put("{" + Definitions.REQUEST_NAMESPACE + "}request", request);
        params.put("{" + Definitions.RESPONSE_NAMESPACE + "}response", response);
        params.put("{" + Definitions.SESSION_NAMESPACE + "}session", request.getSession());
        params.put("{" + Definitions.INFOFUZE_NAMESPACE + "}development-mode", new Boolean(Config.getInstance().isDevelopmentMode()));
        Transformer transformer = new Transformer();
        transformer.setTransformationFile(transformationFile);
        transformer.setParams(params);
        transformer.setTransformMode(TransformMode.NORMAL);
        transformer.setConfiguration(configuration);
        transformer.setErrorListener(new TransformationErrorListener(response));
        transformer.setLogInfo(false);
        DataSourceIf dataSource = new NullSource();
        Document fileInfoDoc = XmlUtils.getEmptyDOM();
        DOMResult result = new DOMResult(fileInfoDoc);
        transformer.transform((Source) dataSource, result);
        Element documentElement = fileInfoDoc.getDocumentElement();
        if (documentElement.getLocalName().equals("null")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        InputStream is = null;
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            String sourceType = XPathUtils.getStringValue(xpath, "source-type", documentElement, null);
            String location = XPathUtils.getStringValue(xpath, "location", documentElement, null);
            String fileName = XPathUtils.getStringValue(xpath, "file-name", documentElement, null);
            String mimeType = XPathUtils.getStringValue(xpath, "mime-type", documentElement, null);
            String encoding = XPathUtils.getStringValue(xpath, "encoding", documentElement, null);
            if (StringUtils.equals(sourceType, "cifsSource")) {
                String domain = XPathUtils.getStringValue(xpath, "domain", documentElement, null);
                String userName = XPathUtils.getStringValue(xpath, "username", documentElement, null);
                String password = XPathUtils.getStringValue(xpath, "password", documentElement, null);
                URI uri = new URI(location);
                if (StringUtils.isNotBlank(userName)) {
                    String userInfo = "";
                    if (StringUtils.isNotBlank(domain)) {
                        userInfo = userInfo + domain + ";";
                    }
                    userInfo = userInfo + userName;
                    if (StringUtils.isNotBlank(password)) {
                        userInfo = userInfo + ":" + password;
                    }
                    uri = new URI(uri.getScheme(), userInfo, uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment());
                }
                SmbFile smbFile = new SmbFile(uri.toURL());
                is = new SmbFileInputStream(smbFile);
            } else if (StringUtils.equals(sourceType, "localFileSystemSource")) {
                File file = new File(location);
                is = new FileInputStream(file);
            } else {
                logger.error("Source type \"" + ((sourceType != null) ? sourceType : "") + "\" not supported");
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            if (StringUtils.isBlank(mimeType) && StringUtils.isBlank(encoding)) {
                response.setContentType(Definitions.MIMETYPE_BINARY);
            } else if (StringUtils.isBlank(encoding)) {
                response.setContentType(mimeType);
            } else {
                response.setContentType(mimeType + ";charset=" + encoding);
            }
            if (request.getParameterMap().containsKey(Definitions.REQUEST_PARAMNAME_DOWNLOAD)) {
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            }
            IOUtils.copy(new BufferedInputStream(is), response.getOutputStream());
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
