    public TextSimilarityMappingPath[] populateDirectly(TextSimilarityMappingPath[] mappingPaths, int pMappingPathsCount, int max_A_Val, int max_B_Val, float[][] pSimilarityArray, int pAlreadyFishedInThisRow_Column, float[] pBlackListWeights) {
        boolean smallerIsFirst = false;
        if (pSimilarityArray[0].length > pSimilarityArray.length) {
            smallerIsFirst = true;
        }
        float[][] simArray = null;
        if (!smallerIsFirst) {
            int firstLen = pSimilarityArray.length;
            int secondLen = pSimilarityArray[0].length;
            simArray = new float[secondLen][firstLen];
            for (int i = 0; i < firstLen; i++) {
                for (int j = 0; j < secondLen; j++) {
                    simArray[j][i] = pSimilarityArray[i][j];
                }
            }
        } else {
            simArray = pSimilarityArray;
        }
        int smallName_len = simArray.length;
        int bigName_len = simArray[0].length;
        size = 0;
        A = new int[smallName_len];
        B = new int[smallName_len];
        similarity = new float[smallName_len];
        boolean[] smallNamePos_Taken = new boolean[smallName_len];
        boolean[] bigNamePos_Taken = new boolean[bigName_len];
        for (int i = 0; i < smallName_len; i++) {
            smallNamePos_Taken[i] = false;
        }
        for (int j = 0; j < bigName_len; j++) {
            bigNamePos_Taken[j] = false;
        }
        float maxValTilNow = 0f;
        int a = -1;
        int b = -1;
        for (int pass = 0; pass < smallName_len; pass++) {
            maxValTilNow = -1f;
            a = -1;
            b = -1;
            for (int i = 0; i < smallName_len; i++) {
                if (!smallNamePos_Taken[i]) {
                    for (int j = 0; j < bigName_len; j++) {
                        if (!bigNamePos_Taken[j]) {
                            if (simArray[i][j] > maxValTilNow) {
                                maxValTilNow = simArray[i][j];
                                a = i;
                                b = j;
                            }
                        }
                    }
                }
            }
            for (int j = 0; j < bigName_len; j++) {
                if (!bigNamePos_Taken[j]) {
                    for (int i = 0; i < smallName_len; i++) {
                        if (!smallNamePos_Taken[i]) {
                            if (simArray[i][j] > maxValTilNow) {
                                maxValTilNow = simArray[i][j];
                                a = i;
                                b = j;
                            }
                        }
                    }
                }
            }
            size++;
            similarity[pass] = simArray[a][b];
            if (smallerIsFirst) {
                A[pass] = a;
                B[pass] = b;
            } else {
                A[pass] = b;
                B[pass] = a;
            }
            smallNamePos_Taken[a] = true;
            bigNamePos_Taken[b] = true;
        }
        return mappingPaths;
    }
