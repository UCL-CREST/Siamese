    private void binSearch(int[] cvToFind, IIntList fieldAccessList) {
        int first = 0;
        int last = buffer.length - 1;
        binSearchFound = false;
        binSearchIdx = -1;
        while (first <= last) {
            int middle = (first + last) / 2;
            int[] cvMiddle = buffer[middle];
            int x = CVCmp.compare(cvToFind, cvMiddle, fieldAccessList);
            if (x == 0) {
                binSearchFound = true;
                binSearchIdx = middle;
                return;
            }
            if (x < 0) {
                last = middle - 1;
            } else {
                binSearchIdx = middle;
                first = middle + 1;
            }
        }
    }
