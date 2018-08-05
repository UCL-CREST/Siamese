    public ClassMetaData getClassMetaData(int classId) {
        int low = 0;
        int high = classes.length - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            ClassMetaData midVal = classes[mid];
            int midValClassId = midVal.classId;
            if (midValClassId < classId) {
                low = mid + 1;
            } else if (midValClassId > classId) {
                high = mid - 1;
            } else {
                return midVal;
            }
        }
        return null;
    }
