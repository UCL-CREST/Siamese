    private int binarySearchExceptionSegments(Segment segment) {
        int low = 0;
        int high = this.exceptionSegments.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Segment midSegment = (Segment) this.exceptionSegments.get(mid);
            if (segment.contains(midSegment) || midSegment.contains(segment)) {
                return mid;
            }
            if (midSegment.before(segment)) {
                low = mid + 1;
            } else if (midSegment.after(segment)) {
                high = mid - 1;
            } else {
                throw new IllegalStateException("Invalid condition.");
            }
        }
        return -(low + 1);
    }
