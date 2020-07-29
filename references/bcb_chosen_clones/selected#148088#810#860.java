    public FieldMetaData getFieldMetaData(String fname) {
        int low = 0;
        int high = realFieldCount - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            FieldMetaData midVal = fields[mid];
            int cmp = midVal.name.compareTo(fname);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return midVal;
            }
        }
        if (horizontalCMD != null) {
            low = 0;
            high = horizontalFields.length - 1;
            while (low <= high) {
                int mid = (low + high) / 2;
                FieldMetaData midVal = horizontalFields[mid];
                int cmp = midVal.name.compareTo(fname);
                if (cmp < 0) {
                    low = mid + 1;
                } else if (cmp > 0) {
                    high = mid - 1;
                } else {
                    return midVal;
                }
            }
        }
        if (horizontalCMD != null) {
            low = 0;
            high = horizontalFields.length - 1;
            while (low <= high) {
                int mid = (low + high) / 2;
                FieldMetaData midVal = horizontalFields[mid];
                int cmp = midVal.origName.compareTo(fname);
                if (cmp < 0) {
                    low = mid + 1;
                } else if (cmp > 0) {
                    high = mid - 1;
                } else {
                    return midVal;
                }
            }
            return null;
        }
        if (pcSuperMetaData == null) return null;
        return pcSuperMetaData.getFieldMetaData(fname);
    }
