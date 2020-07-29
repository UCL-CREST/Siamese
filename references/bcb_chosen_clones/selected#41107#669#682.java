    private static List<String> dependPaths(IModuleManager manager, Module module, Rule rule) {
        String codeText = ModulePathUtil.getCodeText(manager, module, rule);
        int beginIndex = codeText.indexOf("{");
        int endIndex = codeText.lastIndexOf("}");
        codeText = codeText.substring(beginIndex + 1, endIndex);
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\$[\\w\\\\\\[\\]]*");
        Matcher matcher = pattern.matcher(codeText);
        while (matcher.find()) {
            String path = codeText.substring(matcher.start() + 1, matcher.end());
            if (!rule.getName().equals(path) && !list.contains(path)) list.add(path);
        }
        return list;
    }
