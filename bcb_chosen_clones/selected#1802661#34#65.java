    public void setRandom(boolean random) {
        this.random = random;
        if (random) {
            possibleScores = new int[NUM_SCORES];
            for (int i = 0; i < NUM_SCORES - 1; i++) {
                getRandomScore: while (true) {
                    int score = (int) (Math.random() * 20) + 1;
                    for (int j = 0; j < i; j++) {
                        if (score == possibleScores[j]) {
                            continue getRandomScore;
                        }
                    }
                    possibleScores[i] = score;
                    break;
                }
            }
            possibleScores[NUM_SCORES - 1] = 25;
            boolean sorted = false;
            while (!sorted) {
                sorted = true;
                for (int i = 0; i < NUM_SCORES - 1; i++) {
                    if (possibleScores[i] > possibleScores[i + 1]) {
                        int t = possibleScores[i];
                        possibleScores[i] = possibleScores[i + 1];
                        possibleScores[i + 1] = t;
                        sorted = false;
                    }
                }
            }
            setPossibleScores(possibleScores);
        }
    }
