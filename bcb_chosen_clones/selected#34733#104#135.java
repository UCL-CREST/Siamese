    private static boolean found(byte a, byte b) {
        int l = 0, r = n - 1, op = -1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (first[mid] == a) {
                op = mid;
                break;
            }
            if (first[mid] > a) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        if (op == -1) {
            return false;
        }
        l = 0;
        r = num[op];
        while (l <= r) {
            int mid = (l + r) / 2;
            if (second[op][mid] == b) {
                return true;
            }
            if (second[op][mid] > b) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return false;
    }
