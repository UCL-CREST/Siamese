    private boolean isPalindrome(String w) {
        for (int i = 0; i < w.length() / 2; i++) {
            if (w.charAt(i) != w.charAt(w.length() - 1 - i)) {
                return false;
            }
        }
        return true;
    }
