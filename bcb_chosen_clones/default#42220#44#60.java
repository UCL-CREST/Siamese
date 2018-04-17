    public static String readLineWord() {
        String inputString = null, result = null;
        boolean done = false;
        while (!done) {
            inputString = readLine();
            StringTokenizer wordSource = new StringTokenizer(inputString);
            if (wordSource.hasMoreTokens()) {
                result = wordSource.nextToken();
                done = true;
            } else {
                System.out.println("Your input is not correct. Your input must");
                System.out.println("contain at least one nonwhitespace character.");
                System.out.println("Please try again. Enter input:");
            }
        }
        return result;
    }
