    private PieceSet[] getPieceSets() {
        Resource[] resources = boardManager.getResources("pieces");
        PieceSet[] pieceSets = new PieceSet[resources.length];
        for (int i = 0; i < resources.length; i++) pieceSets[i] = (PieceSet) resources[i];
        for (int i = 0; i < resources.length; i++) {
            for (int j = 0; j < resources.length - (i + 1); j++) {
                String name1 = pieceSets[j].getName();
                String name2 = pieceSets[j + 1].getName();
                if (name1.compareTo(name2) > 0) {
                    PieceSet tmp = pieceSets[j];
                    pieceSets[j] = pieceSets[j + 1];
                    pieceSets[j + 1] = tmp;
                }
            }
        }
        return pieceSets;
    }
