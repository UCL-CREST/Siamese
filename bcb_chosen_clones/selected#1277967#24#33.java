    public static boolean checkPalindrome(String string) {
        if (string == null) {
            return false;
        }
        String reverse = new StringBuilder(string).reverse().toString();
        if (string.equals(reverse)) {
            return true;
        }
        return false;
    }
