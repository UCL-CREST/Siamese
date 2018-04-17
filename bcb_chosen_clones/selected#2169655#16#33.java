    public boolean search(T[] collection, T target) {
        if (target == null) {
            return false;
        }
        int low = 0, high = collection.length - 1;
        while (low <= high) {
            int ix = (low + high) / 2;
            int rc = target.compareTo(collection[ix]);
            if (rc < 0) {
                high = ix - 1;
            } else if (rc > 0) {
                low = ix + 1;
            } else {
                return true;
            }
        }
        return false;
    }
