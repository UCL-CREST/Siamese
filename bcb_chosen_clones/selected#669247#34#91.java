    public FriedmanTest(DataCase[][] groups, String[] groupNames) {
        numberGroups = groups.length;
        singleGroupSize = groups[0].length;
        tieMap = new HashMap[numberGroups];
        completeMap = new HashMap[numberGroups];
        hasTie = new boolean[numberGroups];
        maxNumberTies = new int[numberGroups];
        rankGroupAverage = new double[numberGroups];
        rankValues = new double[singleGroupSize][numberGroups];
        rankSum = new double[numberGroups];
        entry = new DataCase[singleGroupSize][numberGroups];
        combo = new DataCase[singleGroupSize][numberGroups];
        for (int i = 0; i < singleGroupSize; i++) {
            for (int j = 0; j < numberGroups; j++) {
                try {
                    entry[i][j] = groups[j][i];
                } catch (Exception e) {
                }
            }
        }
        for (int i = 0; i < singleGroupSize; i++) {
            try {
                combo[i] = QSortAlgorithm.rankList(entry[i]);
            } catch (Exception e) {
            }
            for (int j = 0; j < numberGroups; j++) {
                try {
                    rankValues[i][j] = combo[i][j].getRank();
                } catch (Exception e) {
                }
            }
        }
        newEntry = new DataCase[numberGroups][singleGroupSize];
        for (int i = 0; i < singleGroupSize; i++) {
            for (int j = 0; j < numberGroups; j++) {
                for (int k = 0; k < numberGroups; k++) {
                    if ((combo[i][k].getGroup()).equalsIgnoreCase(groupNames[j])) {
                        rankSum[j] += combo[i][k].getRank();
                        newEntry[j][i] = new DataCase(combo[i][k].getValue(), combo[i][k].getRank(), groupNames[j]);
                    }
                }
            }
        }
        for (int i = 0; i < singleGroupSize; i++) {
            for (int j = 0; j < numberGroups; j++) {
            }
        }
        for (int j = 0; j < numberGroups; j++) {
            try {
                rankGroupAverage[j] = rankSum[j] / (double) singleGroupSize;
            } catch (Exception e) {
            }
        }
        try {
            rankGrandAverage = AnalysisUtility.mean(rankGroupAverage);
        } catch (Exception e) {
        }
    }
