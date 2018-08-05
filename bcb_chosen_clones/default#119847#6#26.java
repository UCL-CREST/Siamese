    public static void main(String[] args) throws IOException {
        String strIn;
        StringBuffer original, reversed;
        char charIn;
        int wrapCount = 0;
        int charCount = 0;
        BufferedReader dataIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Playing with Palindromes\n");
        System.out.print("\nEnter a word, or phrase with no punctuation: ");
        strIn = dataIn.readLine();
        while (strIn.length() > 0) {
            original = new StringBuffer(strIn.trim());
            for (int i = 0; i < original.length(); ++i) if (original.charAt(i) == ' ') original.deleteCharAt(i);
            reversed = new StringBuffer(original.toString());
            reversed.reverse();
            if (original.toString().equalsIgnoreCase(reversed.toString())) System.out.println("Entered string is a palindrome!"); else System.out.println("Entered string is NOT a palindrome!");
            System.out.print("\nEnter a word or phrase with no punctuation: ");
            strIn = dataIn.readLine();
        }
        System.out.println("Program complete.\n");
    }
