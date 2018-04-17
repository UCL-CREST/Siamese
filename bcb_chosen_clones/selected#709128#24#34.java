    public static Double[][] transpor(Double[][] matriz) {
        validarMatriz(matriz);
        Double[][] transposta = new Double[matriz[0].length][matriz.length];
        for (int x = 0; x < matriz.length; x++) {
            Double[] linha = matriz[x];
            for (int y = 0; y < linha.length; y++) {
                transposta[y][x] = matriz[x][y];
            }
        }
        return transposta;
    }
