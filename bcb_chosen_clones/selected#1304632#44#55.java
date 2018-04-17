    public static boolean isPalindrome(String string) {
        int limit = string.length() / 2;
        if (limit == 0) {
            return true;
        }
        for (int forward = 0, backward = string.length() - 1; forward < limit; forward++, backward--) {
            if (string.charAt(forward) != string.charAt(backward)) {
                return false;
            }
        }
        return true;
    }
