    static String getMessageByCode(int errcode) {
        if ((errcode & 0xC0000000) == 0xC0000000) {
            int min = 0;
            int max = NT_STATUS_CODES.length;
            while (max >= min) {
                int mid = (min + max) / 2;
                if (errcode > NT_STATUS_CODES[mid]) {
                    min = mid + 1;
                } else if (errcode < NT_STATUS_CODES[mid]) {
                    max = mid - 1;
                } else {
                    return NT_STATUS_MESSAGES[mid];
                }
            }
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
                    return DOS_ERROR_MESSAGES[mid];
                }
            }
        }
        return "0x" + Hexdump.toHexString(errcode, 8);
    }
