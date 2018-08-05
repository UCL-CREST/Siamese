    private static Comparable binarySearch(ArrayList<? extends Comparable> list, Comparable x) {
        int low = 0;
        int high = list.size() - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (list.get(mid).compareTo(x) < 0) low = mid + 1; else if (list.get(mid).compareTo(x) > 0) high = mid - 1; else return list.get(mid);
        }
        return null;
    }
