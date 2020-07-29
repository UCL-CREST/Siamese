    private int binarySearch(Candidate candidate, Vector counterExample, Boolean[] memoized, int low, int high) throws SETException {
        int testPoint1 = (low + high) / 2;
        int testPoint2 = testPoint1 + 1;
        boolean alpha1, alpha2;
        if (memoized[testPoint1] != null) {
            alpha1 = memoized[testPoint1].booleanValue();
        } else {
            alpha1 = getAlpha(candidate, counterExample, testPoint1);
            memoized[testPoint1] = new Boolean(alpha1);
        }
        if (memoized[testPoint2] != null) {
            alpha2 = memoized[testPoint2].booleanValue();
        } else {
            alpha2 = getAlpha(candidate, counterExample, testPoint2);
            memoized[testPoint2] = new Boolean(alpha2);
        }
        if (alpha1 != alpha2) {
            return (testPoint1);
        } else if (alpha1 == memoized[0].booleanValue()) {
            return (binarySearch(candidate, counterExample, memoized, testPoint1 + 1, high));
        } else {
            return (binarySearch(candidate, counterExample, memoized, low, testPoint1 - 1));
        }
    }
