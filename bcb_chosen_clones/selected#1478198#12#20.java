    public static void main(String[] args) {
        Pattern p2 = Pattern.compile("([a-z](?!100))+");
        Matcher m2 = p2.matcher("ajava100");
        System.out.println(m2.groupCount());
        while (m2.find()) {
            System.out.println(m2.start() + "-" + m2.end());
            System.out.println(m2.group());
        }
    }
