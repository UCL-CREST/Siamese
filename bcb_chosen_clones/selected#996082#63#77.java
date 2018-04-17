    private void choose() {
        final int n = operators.length;
        if (nPick < n) {
            if (unequalWeights) {
                chooseUsingWeights();
            } else {
                for (int k = 0; k < nPick; ++k) {
                    final int which = k + MathUtils.nextInt(n - k);
                    final MCMCOperator tmp = currentRound[k];
                    currentRound[k] = currentRound[which];
                    currentRound[which] = tmp;
                }
            }
        }
    }
