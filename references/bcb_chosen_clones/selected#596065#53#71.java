    static int getStatusByCode(int errcode) {
        if ((errcode & 0xC0000000) == 0xC0000000) {
            return errcode;
        } else {
            int min = 0;
            int max = DOS_ERROR_CODES.length;
            while (max >= min) {
                int mid = (min + max) / 2;
                if (errcode > DOS_ERROR_CODES[mid][0]) {
                    min = mid + 1;
                } else if (errcode < DOS_ERROR_CODES[mid][0]) {
                    max = mid - 1;
                } else {
                    return DOS_ERROR_CODES[mid][1];
                }
            }
        }
        return NT_STATUS_UNSUCCESSFUL;
    }
