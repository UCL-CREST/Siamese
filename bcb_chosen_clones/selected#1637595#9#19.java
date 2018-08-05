    public static void main(String[] args) {
        while (true) {
            Pattern pattern = Pattern.compile("\\S");
            Matcher matcher = pattern.matcher("12*(2 -4) or 32**5->test : exec");
            boolean found = false;
            while (matcher.find()) {
                System.out.println("I found the text \"" + matcher.group() + "\" starting at " + "index " + matcher.start() + " and ending at index " + matcher.end());
                found = true;
            }
        }
    }
