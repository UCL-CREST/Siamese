    private void sort() {
        for (int i = 0; i < density.length; i++) {
            for (int j = density.length - 2; j >= i; j--) {
                if (density[j] > density[j + 1]) {
                    KDNode n = nonEmptyNodesArray[j];
                    nonEmptyNodesArray[j] = nonEmptyNodesArray[j + 1];
                    nonEmptyNodesArray[j + 1] = n;
                    double d = density[j];
                    density[j] = density[j + 1];
                    density[j + 1] = d;
                }
            }
        }
    }
