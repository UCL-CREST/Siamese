    private int GetLongName(CFileInfo curDir, StringRef shortName) {
        int filelist_size = curDir.fileList.size();
        if (filelist_size <= 0) return -1;
        RemoveTrailingDot(shortName);
        int low = 0;
        int high = filelist_size - 1;
        int mid, res;
        while (low <= high) {
            mid = (low + high) / 2;
            res = shortName.value.compareTo(((CFileInfo) curDir.fileList.elementAt(mid)).shortname);
            if (res > 0) low = mid + 1; else if (res < 0) high = mid - 1; else {
                shortName.value = ((CFileInfo) curDir.fileList.elementAt(mid)).orgname;
                return mid;
            }
        }
        return -1;
    }
