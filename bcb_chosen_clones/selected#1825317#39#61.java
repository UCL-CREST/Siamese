    @SuppressWarnings("unchecked")
    protected void displayFreeMarkerResponse(HttpServletRequest request, HttpServletResponse response, String templateName, Map<String, Object> variableMap) throws IOException {
        Enumeration<String> attrNameEnum = request.getSession().getAttributeNames();
        String attrName;
        while (attrNameEnum.hasMoreElements()) {
            attrName = attrNameEnum.nextElement();
            if (attrName != null && attrName.startsWith(ADMIN4J_SESSION_VARIABLE_PREFIX)) {
                variableMap.put("Session" + attrName, request.getSession().getAttribute(attrName));
            }
        }
        variableMap.put("RequestAdmin4jCurrentUri", request.getRequestURI());
        Template temp = FreemarkerUtils.createConfiguredTemplate(this.getClass(), templateName);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            temp.process(variableMap, new OutputStreamWriter(outStream));
            response.setContentLength(outStream.size());
            IOUtils.copy(new ByteArrayInputStream(outStream.toByteArray()), response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            throw new Admin4jRuntimeException(e);
        }
    }
