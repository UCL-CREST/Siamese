    private int[][] checkHomeAwayMatchesUnevenTeamNumber(int[][] matrix) {
        int n = d_clubs.size();
        if (n % 2 == 0) {
            return matrix;
        }
        Stack<Integer> tooManyHomeGames = new Stack<Integer>();
        Stack<Integer> notEnoughHomeGames = new Stack<Integer>();
        int[] numHomeGames = new int[n];
        int expectedHomeGames = (n - 1) / 2;
        for (int i = 0; i < n; i++) {
            notEnoughHomeGames.add(new Integer(i));
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                if (matrix[i][j] <= n) {
                    numHomeGames[i]++;
                    if (numHomeGames[i] == expectedHomeGames) {
                        notEnoughHomeGames.remove(new Integer(i));
                    } else if (numHomeGames[i] > expectedHomeGames) {
                        tooManyHomeGames.push(new Integer(i));
                    }
                }
            }
        }
        while (!tooManyHomeGames.isEmpty()) {
            int i = tooManyHomeGames.pop();
            int j = notEnoughHomeGames.pop();
            while (matrix[i][j] > n) {
                notEnoughHomeGames.add(new Integer(j));
                j = notEnoughHomeGames.pop();
            }
            int tmp = matrix[i][j];
            matrix[i][j] = matrix[j][i];
            matrix[j][i] = tmp;
        }
        return matrix;
    }
