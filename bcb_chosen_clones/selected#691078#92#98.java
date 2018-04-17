    public static String[] separate(String str, String reg) {
        List<String> list = new ArrayList<String>();
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) list.add(str.substring(matcher.start(), matcher.end()));
        return list.toArray(new String[list.size()]);
    }
