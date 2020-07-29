    public static boolean test(String possiblePalindrome) {
        int endOfFirstHalf;
        int startOfSecondHalf;
        if (possiblePalindrome.length() % 2 == 0) {
            endOfFirstHalf = possiblePalindrome.length() / 2;
            startOfSecondHalf = endOfFirstHalf + 1;
        } else {
            endOfFirstHalf = possiblePalindrome.length() / 2;
            startOfSecondHalf = endOfFirstHalf + 2;
        }
        String first = possiblePalindrome.substring(0, endOfFirstHalf);
        String second = possiblePalindrome.substring(startOfSecondHalf - 1);
        return first.equals(reverse(second));
    }
