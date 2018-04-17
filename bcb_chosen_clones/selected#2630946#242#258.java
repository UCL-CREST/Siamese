    private BoardPattern[] getBoardPatterns() {
        Resource[] resources = boardManager.getResources("boards");
        BoardPattern[] boardPatterns = new BoardPattern[resources.length];
        for (int i = 0; i < resources.length; i++) boardPatterns[i] = (BoardPattern) resources[i];
        for (int i = 0; i < resources.length; i++) {
            for (int j = 0; j < resources.length - (i + 1); j++) {
                String name1 = boardPatterns[j].getName();
                String name2 = boardPatterns[j + 1].getName();
                if (name1.compareTo(name2) > 0) {
                    BoardPattern tmp = boardPatterns[j];
                    boardPatterns[j] = boardPatterns[j + 1];
                    boardPatterns[j + 1] = tmp;
                }
            }
        }
        return boardPatterns;
    }
