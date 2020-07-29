    public RoutesMatrix(int cities) {
        theMatrix = new int[cities][cities];
        this.cities = cities;
        Random generator = new Random();
        for (int i = 0; i < cities; i++) {
            for (int j = i; j < cities; j++) {
                if (i == j) theMatrix[i][j] = 0; else {
                    theMatrix[i][j] = generator.nextInt(MAXDISTANCE);
                    theMatrix[j][i] = theMatrix[i][j];
                }
            }
        }
    }
