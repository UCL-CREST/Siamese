    private boolean isPalindrome(int i) {
        String s = String.valueOf(i);
        return s.equals(StringUtils.reverse(s));
    }
