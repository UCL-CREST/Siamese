    public static String convertLocationRun(String sequence, String symbol) {
        String regex = symbol + "+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sequence);
        List<String> ranges = new ArrayList<String>();
        while (matcher.find()) {
            int start = matcher.start() + 1;
            int end = matcher.end();
            ranges.add(start + DELIMITER + end);
        }
        return CStringHelper.join(ranges, ",");
    }
