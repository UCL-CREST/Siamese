    private static boolean isPalindrome(String sub) {
        int length = sub.length() / 2;
        for (int k = 0; k < length; k++) {
            if (sub.charAt(k) != sub.charAt(sub.length() - k - 1)) return false;
        }
        return true;
    }
