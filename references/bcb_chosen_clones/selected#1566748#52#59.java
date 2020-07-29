    private boolean isPalindrome(int[] m) {
        for (int i = 0; i < m.length / 2; i++) {
            if (m[i] != m[m.length - 1 - i]) {
                return false;
            }
        }
        return true;
    }
