    private void insert(Vector list, Comparable value) {
        int left = 0;
        int right = list.size();
        Comparable temp;
        while (left < right) {
            int middle = (left + right) / 2;
            temp = (Comparable) list.get(middle);
            if (temp.compareTo(value) == 0) return;
            if (temp.compareTo(value) > 0) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        list.insertElementAt(value, right);
    }
