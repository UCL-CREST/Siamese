    public static String getHumanPresentation(String propertyName) {
        Pattern pattern = Pattern.compile("\\p{Upper}+");
        Matcher matcher = pattern.matcher(propertyName);
        StringBuffer result = new StringBuffer(propertyName);
        int offset = 0;
        while (matcher.find()) {
            String capitalsLetters = propertyName.substring(matcher.start(), matcher.end());
            if (capitalsLetters.length() == 1) {
                capitalsLetters = capitalsLetters.toLowerCase();
            }
            result.replace(matcher.start() + offset, matcher.end() + offset, " " + capitalsLetters);
            offset++;
        }
        if (result.substring(0, 1).equals(" ")) {
            result.delete(0, 1);
        }
        String firstLetter = result.substring(0, 1).toUpperCase();
        result.replace(0, 1, firstLetter);
        return result.toString();
    }
