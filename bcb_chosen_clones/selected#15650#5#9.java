    public static boolean isPalindrome(String stringToTest) {
        String workingCopyString = removeJunk(stringToTest);
        String reversedCopyString = reverse(stringToTest);
        return reversedCopyString.equalsIgnoreCase(workingCopyString);
    }
