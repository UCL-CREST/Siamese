    public boolean isPalindrome(String s) {
        return new StringBuilder(s).reverse().toString().equals(s);
    }
