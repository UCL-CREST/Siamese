    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map<String, Object> getParameterValues(Map<String, String[]> requestParams, InputStream reportFile) {
        Map<String, Object> ret = new HashMap<String, Object>();
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(reportFile);
            List<Element> list = document.getRootElement().elements("parameter");
            for (String param : requestParams.keySet()) {
                if (DynamicReport.REPORT_FILE.equals(param) || DynamicReport.REPORT_FORMAT.equals(param)) continue;
                for (Element elem : list) {
                    Attribute attrib = elem.attribute("name");
                    if (attrib.getValue().equals(param)) {
                        Attribute attrClass = elem.attribute("class");
                        String className = "java.lang.String";
                        String[] valueArr = requestParams.get(param);
                        String value = StringUtil.explode(',', valueArr);
                        if (StringUtil.isEmpty(value)) continue;
                        if (attrClass != null) className = attrClass.getValue();
                        try {
                            if ("java.io.InputStream".equals(className)) {
                                File image = new File(jasperPath + value);
                                InputStream stream = new FileInputStream(image);
                                if (stream == null) _log.warn("Unable to find file [" + jasperPath + value + "]"); else ret.put(param, stream);
                            } else if ("java.util.List".equals(className)) {
                                if (value != null) {
                                    String[] values = value.split(",");
                                    List<String> temp = Arrays.asList(values);
                                    ret.put(param, temp);
                                }
                            } else {
                                Class classDefinition = Class.forName(className);
                                Object objectValue = classDefinition.getConstructor(String.class).newInstance(value);
                                ret.put(param, objectValue);
                            }
                        } catch (Exception e) {
                            _log.error("Failed to parse parameter [" + param + "] with value [" + value + "].", e);
                        }
                    }
                }
            }
        } catch (DocumentException e) {
            _log.error(e, e);
        }
        return ret;
    }
