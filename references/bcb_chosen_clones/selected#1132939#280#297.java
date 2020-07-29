    private String replaceI18nKeyToValue(final String codetext) {
        i18nKeys.clear();
        String name = module.getName();
        Pattern pattern = Pattern.compile("#CT[0-9]{6}");
        Matcher matcher = pattern.matcher(codetext);
        String key;
        StringBuffer sb = new StringBuffer();
        int offset = 0;
        while (matcher.find()) {
            sb.append(codetext.substring(offset, matcher.start()));
            key = codetext.substring(matcher.start() + 1, matcher.end());
            i18nKeys.add(key);
            sb.append("`").append(I18nManagerImpl.getProperty(name, key)).append("`");
            offset = matcher.end();
        }
        sb.append(codetext.substring(offset));
        return sb.toString();
    }
