    static String getMessageByWinerrCode(int errcode) {
        int min = 0;
        int max = WINERR_CODES.length;
        while (max >= min) {
            int mid = (min + max) / 2;
            if (errcode > WINERR_CODES[mid]) {
                min = mid + 1;
            } else if (errcode < WINERR_CODES[mid]) {
                max = mid - 1;
            } else {
                return WINERR_MESSAGES[mid];
            }
        }
        return errcode + "";
    }
