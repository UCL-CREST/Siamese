    private double[][] calcAdjoint(double[][] values, int size, GlobalValues globals) {
        FunctionToken token = null;
        Function function = null;
        try {
            token = new FunctionToken("Determinant");
            function = globals.getFunctionManager().findFunction(token);
        } catch (java.lang.Exception e) {
        }
        double[][] result = new double[size][size];
        for (int rowNumber = 0; rowNumber < size; rowNumber++) {
            for (int colNumber = 0; colNumber < size; colNumber++) {
                DoubleNumberToken subMatrix = new DoubleNumberToken(constructMatrix(values, size, rowNumber, colNumber));
                OperandToken[] operands = new OperandToken[1];
                operands[0] = subMatrix;
                double minor = ((DoubleNumberToken) function.evaluate(operands, globals)).getValueRe();
                int modifier = -1;
                if ((rowNumber + colNumber) % 2 == 0) modifier = 1;
                result[rowNumber][colNumber] = modifier * minor;
            }
        }
        double[][] transResult = new double[size][size];
        for (int colno = 0; colno < size; colno++) {
            for (int rowno = 0; rowno < size; rowno++) {
                transResult[colno][rowno] = result[rowno][colno];
            }
        }
        return transResult;
    }
