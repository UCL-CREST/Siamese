    public static String extractProperties(String value) {
        if (value == null) {
            return null;
        }
        String result = value;
        Pattern pattern = Pattern.compile("\\$\\{[^}]*\\}");
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            String propertyName = value.substring(start, end);
            String name = value.substring(start + 2, end - 1);
            result = result.replace(propertyName, getPropertyValue(name));
        }
        return result;
    }
