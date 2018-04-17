    private static boolean checkPalindrome(String string) {
        int length = string.length() / 2;
        int fullLength = string.length();
        for (int k = 0; k < length; k++) {
            if (string.charAt(k) != string.charAt(fullLength - 1 - k)) return false;
        }
        return true;
    }
