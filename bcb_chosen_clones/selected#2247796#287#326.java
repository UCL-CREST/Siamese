    private int indexOfFisrtNameAfter(int position) {
        int left = 0;
        int right = this.potentialVariableNamesPtr;
        next: while (true) {
            if (right < left) return -1;
            int mid = left + (right - left) / 2;
            int midPosition = this.potentialVariableNameStarts[mid];
            if (midPosition < 0) {
                int nextMid = indexOfNextName(mid);
                if (nextMid < 0 || right < nextMid) {
                    right = mid - 1;
                    continue next;
                }
                mid = nextMid;
                midPosition = this.potentialVariableNameStarts[nextMid];
                if (mid == right) {
                    int leftPosition = this.potentialVariableNameStarts[left];
                    if (leftPosition < 0 || leftPosition < position) {
                        int nextLeft = indexOfNextName(left);
                        if (nextLeft < 0) return -1;
                        left = nextLeft;
                        continue next;
                    }
                    return left;
                }
            }
            if (left != right) {
                if (midPosition < position) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            } else {
                if (midPosition < position) {
                    return -1;
                }
                return mid;
            }
        }
    }
