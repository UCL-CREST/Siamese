    public static String hump2ohter(String param, String aother) {
        char other = aother.toCharArray()[0];
        Pattern p = Pattern.compile("[A-Z]");
        if (param == null || param.equals("")) {
            return "";
        }
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, other + mc.group().toLowerCase());
            i++;
        }
        if (other == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }
