    public int[][] simulate() {
        int[][] data = new int[n][loci];
        int[][] tetrad = new int[4][loci];
        int[] iAndj = new int[2];
        for (int i = 0; i < n; i++) {
            tetrad = new int[4][loci];
            tetrad[0][0] = 1;
            tetrad[1][0] = 1;
            tetrad[2][0] = 0;
            tetrad[3][0] = 0;
            for (int index = 1; index < loci; index++) {
                iAndj = getIandJ(cProb[index - 1]);
                int swap = 0;
                switch(iAndj[0]) {
                    case 0:
                        tetrad[0][index] = tetrad[0][index - 1];
                        tetrad[1][index] = tetrad[1][index - 1];
                        tetrad[2][index] = tetrad[2][index - 1];
                        tetrad[3][index] = tetrad[3][index - 1];
                        break;
                    case 1:
                        tetrad[0][index] = tetrad[2][index - 1];
                        tetrad[1][index] = tetrad[1][index - 1];
                        tetrad[2][index] = tetrad[0][index - 1];
                        tetrad[3][index] = tetrad[3][index - 1];
                        break;
                    case 2:
                        tetrad[0][index] = tetrad[0][index - 1];
                        tetrad[1][index] = tetrad[2][index - 1];
                        tetrad[2][index] = tetrad[1][index - 1];
                        tetrad[3][index] = tetrad[3][index - 1];
                        break;
                    case 3:
                        tetrad[0][index] = tetrad[0][index - 1];
                        tetrad[1][index] = tetrad[3][index - 1];
                        tetrad[2][index] = tetrad[2][index - 1];
                        tetrad[3][index] = tetrad[1][index - 1];
                        break;
                    case 4:
                        tetrad[0][index] = tetrad[3][index - 1];
                        tetrad[1][index] = tetrad[1][index - 1];
                        tetrad[2][index] = tetrad[2][index - 1];
                        tetrad[3][index] = tetrad[0][index - 1];
                        break;
                }
                switch(iAndj[1]) {
                    case 0:
                        tetrad[0][index] = tetrad[0][index];
                        tetrad[1][index] = tetrad[1][index];
                        tetrad[2][index] = tetrad[2][index];
                        tetrad[3][index] = tetrad[3][index];
                        break;
                    case 1:
                        swap = tetrad[0][index];
                        tetrad[0][index] = tetrad[2][index];
                        tetrad[1][index] = tetrad[1][index];
                        tetrad[2][index] = swap;
                        tetrad[3][index] = tetrad[3][index];
                        break;
                    case 2:
                        swap = tetrad[1][index];
                        tetrad[0][index] = tetrad[0][index];
                        tetrad[1][index] = tetrad[2][index];
                        tetrad[2][index] = swap;
                        tetrad[3][index] = tetrad[3][index];
                        break;
                    case 3:
                        swap = tetrad[1][index];
                        tetrad[0][index] = tetrad[0][index];
                        tetrad[1][index] = tetrad[3][index];
                        tetrad[2][index] = tetrad[2][index];
                        tetrad[3][index] = swap;
                        break;
                    case 4:
                        swap = tetrad[0][index];
                        tetrad[0][index] = tetrad[3][index];
                        tetrad[1][index] = tetrad[1][index];
                        tetrad[2][index] = tetrad[2][index];
                        tetrad[3][index] = swap;
                        break;
                }
            }
            data[i] = tetrad[randomIndex()];
        }
        int[][] rflp = new int[data[0].length][data.length];
        for (int index1 = 0; index1 < data[0].length; index1++) {
            for (int index2 = 0; index2 < data.length; index2++) {
                rflp[index1][index2] = data[index2][index1];
            }
        }
        return rflp;
    }
