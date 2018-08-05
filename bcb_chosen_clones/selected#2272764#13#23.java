    public static void main(String[] args) {
        String str = "Ha32439noooooimua nay haaaa3noi vang nhung con mua hhhhhnoi.";
        System.out.print("Reg:");
        Scanner scan = new Scanner(System.in);
        String reg = scan.next();
        Pattern patern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher matcher = patern.matcher(str);
        while (matcher.find()) {
            System.out.println("Found '" + reg + "' at (" + matcher.start() + "," + matcher.end() + "): " + matcher.group());
        }
    }
