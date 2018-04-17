    public static void main(String[] args) {
        RegexUtil util = new RegexUtil();
        String strPattern1 = "(.*)([\\d+])(.*)";
        String strText1 = "TR[54]";
        Pattern pattern = Pattern.compile(strPattern1);
        Matcher matcher = pattern.matcher(strText1);
        System.out.println("Matches entire String " + matcher.matches());
        System.out.println("Matches at beginning " + matcher.lookingAt());
        System.out.println(matcher.group(1));
        while (matcher.find()) {
            System.out.println("Found a match: " + matcher.group());
            System.out.println("Start position: " + matcher.start());
            System.out.println("End position: " + matcher.end());
        }
        String strText2 = "abasdfABSDSAFASDF";
        System.out.println(!strText2.matches("[a-zA-Z]*"));
    }
