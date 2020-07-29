    public static String getFormattedName(final InformationUnit unit) {
        StringWriter returnValue = new StringWriter();
        String string = ContactActivator.getDefault().getPreferenceStore().getString(ContactPreferenceInitializer.FORMATTED_NAME_PATTERN);
        Pattern compile = Pattern.compile(FORMATTED_REGEXP);
        Matcher matcher = compile.matcher(string);
        int lastEnd = 0;
        while (matcher.find()) {
            String group = matcher.group();
            matcher.start();
            returnValue.append(string.substring(lastEnd, matcher.start()));
            lastEnd = matcher.end();
            String substring = group.substring(1);
            InformationUnit childByType = InformationUtil.getChildByType(unit, substring);
            if (childByType != null && childByType.getStringValue() != null) {
                returnValue.append(childByType.getStringValue());
            }
        }
        return StringUtils.strip(returnValue.toString());
    }
