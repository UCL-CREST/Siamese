    public static boolean isPalindrome(String numberString) {
        if (numberString.length() % 2 == 0) {
            String firstHalf = numberString.substring(0, numberString.length() / 2);
            String secondHalf = numberString.substring(numberString.length() / 2);
            String secondHalfReverse = (new StringBuffer(secondHalf)).reverse().toString();
            return firstHalf.equals(secondHalfReverse);
        }
        return false;
    }
