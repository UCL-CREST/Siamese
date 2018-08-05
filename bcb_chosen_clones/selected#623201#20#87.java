    public int[] sort() {
        int i, tmp;
        int[] newIndex = new int[nrows];
        for (i = 0; i < nrows; i++) {
            newIndex[i] = i;
        }
        boolean change = true;
        if (this.ascending) {
            if (data[0][column] instanceof Comparable) {
                while (change) {
                    change = false;
                    for (i = 0; i < nrows - 1; i++) {
                        if (((Comparable) data[newIndex[i]][column]).compareTo((Comparable) data[newIndex[i + 1]][column]) > 0) {
                            tmp = newIndex[i];
                            newIndex[i] = newIndex[i + 1];
                            newIndex[i + 1] = tmp;
                            change = true;
                        }
                    }
                }
                return newIndex;
            }
            if (data[0][column] instanceof String || data[0][column] instanceof ClassLabel) {
                while (change) {
                    change = false;
                    for (i = 0; i < nrows - 1; i++) {
                        if ((data[newIndex[i]][column].toString()).compareTo(data[newIndex[i + 1]][column].toString()) > 0) {
                            tmp = newIndex[i];
                            newIndex[i] = newIndex[i + 1];
                            newIndex[i + 1] = tmp;
                            change = true;
                        }
                    }
                }
            }
            return newIndex;
        }
        if (!this.ascending) {
            if (data[0][column] instanceof Comparable) {
                while (change) {
                    change = false;
                    for (i = 0; i < nrows - 1; i++) {
                        if (((Comparable) data[newIndex[i]][column]).compareTo((Comparable) data[newIndex[i + 1]][column]) < 0) {
                            tmp = newIndex[i];
                            newIndex[i] = newIndex[i + 1];
                            newIndex[i + 1] = tmp;
                            change = true;
                        }
                    }
                }
                return newIndex;
            }
            if (data[0][column] instanceof String || data[0][column] instanceof ClassLabel) {
                while (change) {
                    change = false;
                    for (i = 0; i < nrows - 1; i++) {
                        if ((data[newIndex[i]][column].toString()).compareTo(data[newIndex[i + 1]][column].toString()) < 0) {
                            tmp = newIndex[i];
                            newIndex[i] = newIndex[i + 1];
                            newIndex[i + 1] = tmp;
                            change = true;
                        }
                    }
                }
            }
            return newIndex;
        } else return newIndex;
    }
