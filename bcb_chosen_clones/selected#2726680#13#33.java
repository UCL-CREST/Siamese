    public static void main(String[] args) {
        String email1 = "hoangpt@gmail.com";
        String email2 = "asdfasd";
        String regex1 = "\\w*@[A-Za-z_1-9]+\\.\\w[3]";
        String regex3 = "^a.*b$";
        String str1 = "adfdsfb";
        String regex4 = "abc(xyz|hyt)";
        String regex5 = "[a-e]{4}&&[u-v]*";
        String regex6 = "(\\w*)\1";
        System.out.printf("%1$-7s", regex4);
        assert regex5 == "adv" : "Not right";
        String str = "Hhhhhnoiqii mua nay hanodooiminnnnnnni vang haaannnoiiiii nhung con mua.";
        String regex2 = "h[a-e]?n[^d-f]+i.";
        System.out.print("Enter regex:");
        String regex = new Scanner(System.in).next();
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            System.out.println("String '" + matcher.group() + "' is found at [" + matcher.start() + "," + matcher.end() + "]");
        }
    }
