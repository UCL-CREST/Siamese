    public int getTheNearestElementAfter(int row) {
        if (row < 0) {
            throw new IllegalArgumentException("negative value is not a valid value for a row");
        }
        int rowAfter = -1;
        if (!listOfRow.isEmpty()) {
            int currentIndex = index;
            try {
                int startAt = 0;
                int stopAt = listOfRow.size() - 1;
                int middle = 0;
                boolean rowInTheList = false;
                while (!rowInTheList && startAt <= stopAt) {
                    middle = (startAt + stopAt) / 2;
                    rowAfter = listOfRow.getRow(middle);
                    rowInTheList = (rowAfter == row);
                    if (rowAfter > row) {
                        stopAt = middle - 1;
                    } else if (rowAfter < row) {
                        startAt = middle + 1;
                    }
                }
                index = middle;
                if (!rowInTheList) {
                    if (rowAfter < row) {
                        rowAfter = getNextElement();
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                index = currentIndex;
                rowAfter = -1;
            }
        }
        return rowAfter;
    }
