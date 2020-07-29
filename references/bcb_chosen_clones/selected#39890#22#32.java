    private String calculate(final String from, final Resources resources) {
        StringBuffer output = new StringBuffer();
        Pattern resourceRegexp = Pattern.compile("\\$\\{[\\w\\.]+\\}");
        Matcher matcher = resourceRegexp.matcher(from);
        while (matcher.find()) {
            String resourceKey = from.substring(matcher.start() + 2, matcher.end() - 1);
            matcher.appendReplacement(output, resources.getResourceValue(resourceKey));
        }
        matcher.appendTail(output);
        return output.toString();
    }
