    public static boolean isPalindrome(String word) {
        boolean result = false;
        if (word.length() <= 1) result = true; else if (word.charAt(0) == word.charAt(word.length() - 1)) result = isPalindrome(word.substring(1, word.length() - 1));
        return result;
    }
