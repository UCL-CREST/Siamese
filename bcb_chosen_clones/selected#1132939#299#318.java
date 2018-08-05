    private String replaceI18nValueToKey(final String codetext) {
        String name = module.getName();
        Pattern pattern = Pattern.compile("`[^`]*`");
        Matcher matcher = pattern.matcher(codetext);
        String key, i18nValue;
        StringBuffer sb = new StringBuffer();
        int keyIndex = 0, offset = 0;
        while (matcher.find()) {
            sb.append(codetext.substring(offset, matcher.start()));
            i18nValue = codetext.substring(matcher.start() + 1, matcher.end() - 1);
            if (keyIndex < i18nKeys.size()) {
                key = i18nKeys.get(keyIndex++);
                I18nManagerImpl.setProperty(name, key, i18nValue);
            } else key = I18nManagerImpl.getI18nKey(module, "CT", i18nValue);
            sb.append("#").append(key);
            offset = matcher.end();
        }
        sb.append(codetext.substring(offset));
        return sb.toString();
    }
