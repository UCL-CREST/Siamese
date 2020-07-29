    public int getTheNearestElementBefore(int row) {
        if (row < 0) {
            throw new IllegalArgumentException("negative value is not a valid value for a row");
        }
        int rowBefore = -1;
        if (!listOfRow.isEmpty()) {
            int currentIndex = index;
            try {
                int startAt = 0;
                int stopAt = listOfRow.size() - 1;
                int middle = 1;
                boolean rowInTheList = false;
                while (!rowInTheList && startAt <= stopAt) {
                    middle = (startAt + stopAt) / 2;
                    rowBefore = listOfRow.getRow(middle);
                    rowInTheList = (rowBefore == row);
                    if (rowBefore > row) {
                        stopAt = middle - 1;
                    } else if (rowBefore < row) {
                        startAt = middle + 1;
                    }
                }
                index = middle;
                if (!rowInTheList) {
                    if (rowBefore > row) {
                        rowBefore = getPreviousElement();
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                index = currentIndex;
                rowBefore = -1;
            }
        }
        return rowBefore;
    }
