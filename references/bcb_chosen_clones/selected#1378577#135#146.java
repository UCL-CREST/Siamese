    private static void testRepetitions() {
        String type = "V2";
        Pattern number = Pattern.compile("([0-9]*)");
        Matcher matcher = number.matcher(type);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            System.out.print(start + ":");
            System.out.print(end + "=>");
            System.out.println(type.substring(start, end));
        }
    }
