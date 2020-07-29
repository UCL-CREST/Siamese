    public static boolean isPalindrome(String str) {
        int begin = 0;
        int end = str.length() - 1;
        if (str == null) return false;
        while (begin < (int) (str.length() / 2)) {
            if (str.charAt(begin) != str.charAt(end)) return false; else {
                begin++;
                end--;
            }
        }
        return true;
    }
