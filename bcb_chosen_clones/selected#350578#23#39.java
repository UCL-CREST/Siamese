    public SequenceMutator(Short substitutionMatrix[][]) {
        matrixOrder = "ABCDEFGHIKLMNPQRSTVWXYZ-";
        transitionMatrix = new double[matrixOrder.length()][matrixOrder.length()];
        double transMatRaw[][] = new double[matrixOrder.length()][matrixOrder.length()], columnSubotals[] = new double[matrixOrder.length()], probabilitySubtotal[] = new double[matrixOrder.length()];
        for (int i = 0; i < transMatRaw.length; i++) for (int j = 0; j < transMatRaw[i].length; j++) {
            if (i == transMatRaw.length - 1) if (j == transMatRaw.length - 1) transMatRaw[i][j] = Math.exp(0); else transMatRaw[i][j] = Math.exp(-2); else if (j == transMatRaw.length - 1) transMatRaw[i][j] = Math.exp(-2); else transMatRaw[i][j] = Math.exp(substitutionMatrix[i < j ? j : i][i < j ? i : j]);
        }
        for (int i = 0; i < transMatRaw.length; i++) for (int j = 0; j <= i; j++) columnSubotals[j] += transMatRaw[i][j];
        for (int j = 0; j < columnSubotals.length; j++) {
            for (int i = 0; i < columnSubotals.length; i++) {
                if (i < j) {
                    transitionMatrix[i][j] = transitionMatrix[j][i];
                    probabilitySubtotal[j] += transitionMatrix[j][i];
                } else transitionMatrix[i][j] = (1 - probabilitySubtotal[j]) * (transMatRaw[i][j] / columnSubotals[j]);
            }
        }
    }
