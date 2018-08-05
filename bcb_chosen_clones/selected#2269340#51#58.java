    private static String readStreamToString(InputStream is, boolean passInVelocity, String tplName, Map<String, Object> templateVarsMap) throws IOException {
        StringWriter sw = new StringWriter();
        IOUtils.copy(is, sw, "UTF-8");
        if (passInVelocity) {
            return tpl.formatStr(sw.toString(), templateVarsMap, tplName);
        }
        return sw.toString();
    }
