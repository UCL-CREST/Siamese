    public static String[] parseActionCode(String input) {
        ArrayList<String> spliter = new ArrayList<String>();
        ArrayList<String> content = new ArrayList<String>();
        Pattern pattern = Pattern.compile("([$][0-9]+|[$][$])");
        Matcher matcher = pattern.matcher(input);
        int index = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (start >= index) {
                content.add(input.substring(index, start));
            }
            spliter.add(input.substring(start + 1, end));
            index = end;
        }
        if (index < input.length()) content.add(input.substring(index));
        String[] ret = new String[spliter.size() + content.size()];
        for (int i = 0; i < ret.length; ++i) {
            if ((i % 2) == 0) ret[i] = content.get(i / 2); else ret[i] = spliter.get(i / 2);
        }
        return ret;
    }
