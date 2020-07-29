    @SuppressWarnings("unchecked")
    protected void processTransformAction(HttpServletRequest request, HttpServletResponse response, String action) throws Exception {
        File transformationFile = null;
        String tr = request.getParameter(Definitions.REQUEST_PARAMNAME_XSLT);
        if (StringUtils.isNotBlank(tr)) {
            transformationFile = new File(xslBase, tr);
            if (!transformationFile.isFile()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parameter \"" + Definitions.REQUEST_PARAMNAME_XSLT + "\" " + "with value \"" + tr + "\" refers to non existing file");
                return;
            }
        }
        StreamResult result;
        ByteArrayOutputStream baos = null;
        if (isDevelopmentMode) {
            baos = new ByteArrayOutputStream();
            if (StringUtils.equals(action, "get")) {
                result = new StreamResult(new Base64.OutputStream(baos, Base64.DECODE));
            } else {
                result = new StreamResult(baos);
            }
        } else {
            if (StringUtils.equals(action, "get")) {
                result = new StreamResult(new Base64.OutputStream(response.getOutputStream(), Base64.DECODE));
            } else {
                result = new StreamResult(response.getOutputStream());
            }
        }
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
        String method = transformer.getOutputProperties().getProperty(OutputKeys.METHOD, "xml");
        String contentType;
        if (method.endsWith("html")) {
            contentType = Definitions.MIMETYPE_HTML;
        } else if (method.equals("xml")) {
            contentType = Definitions.MIMETYPE_XML;
        } else {
            contentType = Definitions.MIMETYPE_TEXTPLAIN;
        }
        String encoding = transformer.getOutputProperties().getProperty(OutputKeys.ENCODING, "UTF-8");
        response.setContentType(contentType + ";charset=" + encoding);
        DataSourceIf dataSource = new NullSource();
        transformer.transform((Source) dataSource, result);
        if (isDevelopmentMode) {
            IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
        }
    }
