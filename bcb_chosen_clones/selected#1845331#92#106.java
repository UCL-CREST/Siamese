    public static InstructionHandle findHandle(InstructionHandle[] ihs, int[] pos, int count, int target) {
        int l = 0, r = count - 1;
        do {
            int i = (l + r) / 2;
            int j = pos[i];
            if (j == target) {
                return ihs[i];
            } else if (target < j) {
                r = i - 1;
            } else {
                l = i + 1;
            }
        } while (l <= r);
        return null;
    }
