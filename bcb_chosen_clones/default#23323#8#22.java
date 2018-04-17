    public int minimumSwaps(String[] grid, String word) {
        int n = word.length();
        char[][] charGrid = new char[n][];
        for (int i = 0; i < n; i++) charGrid[i] = grid[i].toCharArray();
        charMap = new HashMap<Character, Integer>();
        for (int i = 0; i < n; i++) charMap.put(word.charAt(i), i);
        int minrow = minSwaps(charGrid);
        for (int i = 0; i < n - 1; i++) for (int j = i + 1; j < n; j++) {
            char temp = charGrid[i][j];
            charGrid[i][j] = charGrid[j][i];
            charGrid[j][i] = temp;
        }
        int mincol = minSwaps(charGrid);
        if (minrow < 0) return mincol; else if (mincol < 0) return minrow; else return Math.min(minrow, mincol);
    }
