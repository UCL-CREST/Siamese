    private int binarySearch(LinkedList<Edge> list, float value) {
        int lower = 0, middle, upper = list.size() - 1;
        while (upper >= lower) {
            middle = (upper + lower) / 2;
            int result = Float.compare(value, list.get(middle).getValue());
            if (result > 0) lower = middle + 1; else if (result < 0) upper = middle - 1; else return middle;
        }
        return lower;
    }
