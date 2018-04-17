    private static JsniJavaRefReplacementResult replaceJsniJavaRefs(String jsni) {
        Map<String, String> replacements = new HashMap<String, String>();
        Pattern p = Pattern.compile("@[a-zA-Z0-9._$]+::[a-zA-Z0-9_$]+(\\(.*?\\)\\(.*?\\))?");
        Matcher m = p.matcher(jsni);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            String ref = jsni.substring(start, end);
            String jsToken = makeJsToken(ref);
            while (replacements.containsKey(jsToken) && !replacements.get(jsToken).equals(ref)) {
                jsToken = makeJsToken(jsToken);
            }
            replacements.put(jsToken, ref);
        }
        for (Entry<String, String> kvp : replacements.entrySet()) {
            jsni = jsni.replace(kvp.getValue(), kvp.getKey());
        }
        return new JsniJavaRefReplacementResult(jsni, replacements);
    }
