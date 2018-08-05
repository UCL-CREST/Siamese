    public int getIndexOfRow(int row) {
        if (row < 0) {
            throw new IllegalArgumentException("negative value is not a valid value for a row");
        }
        int middle = 0;
        boolean found = false;
        if (list != null && !isEmpty()) {
            int startAt = 0;
            int stopAt = size - 1;
            int result = -1;
            while (!found && startAt <= stopAt) {
                middle = (startAt + stopAt) / 2;
                result = list[middle];
                if (result == row) {
                    found = true;
                } else {
                    if (result > row) {
                        stopAt = middle - 1;
                    } else if (result < row) {
                        startAt = middle + 1;
                    }
                }
            }
        }
        if (!found) {
            middle = -1;
        }
        return middle;
    }
