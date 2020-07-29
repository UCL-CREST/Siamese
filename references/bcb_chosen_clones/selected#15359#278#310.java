    protected Integer insert(TreeMap<Integer, Integer> thresh, Integer j, Integer k) {
        if (isNonzero(k) && isGreaterThan(thresh, k, j) && isLessThan(thresh, k - 1, j)) {
            thresh.put(k, j);
        } else {
            int highIndex = -1;
            if (isNonzero(k)) {
                highIndex = k;
            } else if (!thresh.isEmpty()) {
                highIndex = thresh.lastKey();
            }
            if (highIndex == -1 || j.compareTo(thresh.get(thresh.lastKey())) > 0) {
                append(thresh, j);
                k = highIndex + 1;
            } else {
                int lowIndex = 0;
                while (lowIndex <= highIndex) {
                    int index = (highIndex + lowIndex) / 2;
                    Integer val = thresh.get(index);
                    int compareResult = j.compareTo(val);
                    if (compareResult == 0) {
                        return null;
                    } else if (compareResult > 0) {
                        lowIndex = index + 1;
                    } else {
                        highIndex = index - 1;
                    }
                }
                thresh.put(lowIndex, j);
                k = lowIndex;
            }
        }
        return k;
    }
