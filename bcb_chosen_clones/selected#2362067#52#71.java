    public static FieldBinding binarySearch(char[] name, FieldBinding[] sortedFields) {
        if (sortedFields == null) return null;
        int max = sortedFields.length;
        if (max == 0) return null;
        int left = 0, right = max - 1, nameLength = name.length;
        int mid = 0;
        char[] midName;
        while (left <= right) {
            mid = left + (right - left) / 2;
            int compare = compare(name, midName = sortedFields[mid].name, nameLength, midName.length);
            if (compare < 0) {
                right = mid - 1;
            } else if (compare > 0) {
                left = mid + 1;
            } else {
                return sortedFields[mid];
            }
        }
        return null;
    }
