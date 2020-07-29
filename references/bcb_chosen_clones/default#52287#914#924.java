    public void Reveal(int x1, int x2, int y1, int y2) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                if (WasItCorrect(i, j) == false) {
                    ColorMatrix[i][j] = 1;
                    gGuesses[i][j] = answers[j][i];
                    NumberCorrectAnswers++;
                }
            }
        }
    }
