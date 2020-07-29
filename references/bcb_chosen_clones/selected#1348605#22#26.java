    private static final boolean isPalindrome(final String s) {
        String opposite = "";
        for (int i = s.length() - 1; i >= 0; i--) opposite = opposite + s.charAt(i);
        return s.equals(opposite);
    }
