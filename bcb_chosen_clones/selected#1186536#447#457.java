    private static String convertIntegerStringToUnicodeString(String input) {
        StringBuffer sb = new StringBuffer(input);
        Pattern numbers = Pattern.compile("0x[0-9a-fA-F]{4}");
        Matcher matcher = numbers.matcher(sb);
        while (matcher.find()) {
            char a = (char) (Integer.decode(matcher.group()).intValue());
            sb.replace(matcher.start(), matcher.end(), String.valueOf(a));
            matcher.reset();
        }
        return sb.toString();
    }
