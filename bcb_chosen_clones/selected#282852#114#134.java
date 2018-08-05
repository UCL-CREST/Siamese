    public static int getByLastName(PlayerFileManager[] arrayList, int len, String quarry, boolean startsWith) {
        if (quarry == null) return -1;
        quarry = quarry.toLowerCase();
        int start = 0;
        int end = len - 1;
        int mid = end / 2;
        int found = -1;
        String s = "";
        while (start <= end && found == -1) {
            s = combineNames(arrayList[mid], false).toLowerCase();
            if ((startsWith && s.startsWith(quarry)) || s.equalsIgnoreCase(quarry)) {
                found = mid;
            } else if (quarry.compareToIgnoreCase(s) < 0) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
            mid = (start + end) / 2;
        }
        return found;
    }
