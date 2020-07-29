    public DistanceMatrix(DistanceMatrix dm, IdGroup subset) {
        int index1, index2;
        distance = new double[subset.getIdCount()][subset.getIdCount()];
        for (int i = 0; i < distance.length; i++) {
            index1 = dm.whichIdNumber(subset.getIdentifier(i).getName());
            distance[i][i] = dm.distance[index1][index1];
            for (int j = 0; j < i; j++) {
                index2 = dm.whichIdNumber(subset.getIdentifier(j).getName());
                distance[i][j] = dm.distance[index1][index2];
                distance[j][i] = distance[i][j];
            }
        }
        idGroup = subset;
    }
