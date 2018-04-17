    private String replacePlaceholders(String key, String value) throws ObclipseException {
        Pattern fsPattern = Pattern.compile("\\$\\{[^(\\$\\{)^\\}]*\\}");
        Matcher matcher = fsPattern.matcher(value);
        String newValue = new String();
        int index = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            newValue += value.subSequence(index, start);
            String placeholder = value.substring(start + 2, end - 1);
            if (key.equals(placeholder)) {
                throw new ObclipseException("It is not possible to use a property key inside its poperty value as placeholder! Key: " + key);
            }
            String placeholderReplaceValue = System.getProperty(placeholder);
            if (placeholderReplaceValue == null) {
                Object object = _properties.get(placeholder);
                if (object != null) {
                    placeholderReplaceValue = (String) object;
                }
            }
            if (placeholderReplaceValue != null) {
                newValue += PropertiesUtil.trimEnclosingQuotes(placeholderReplaceValue);
            } else {
                throw new ObclipseException("Cannot replace placeholder '" + placeholder + "'! This placeholder property key is not defined!");
            }
            index = end;
        }
        newValue += value.subSequence(index, value.length());
        return newValue;
    }
