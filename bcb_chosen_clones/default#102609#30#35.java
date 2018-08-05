    private static boolean isPalindrome(String string) {
        for (int k = 0; k < string.length() / 2; k++) {
            if (string.charAt(k) != string.charAt(string.length() - (k + 1))) return false;
        }
        return true;
    }
