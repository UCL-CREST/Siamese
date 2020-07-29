        int find(A key) {
            int low = 0;
            int high = keys.size() - 1;
            while (low <= high) {
                int mid = (low + high) / 2;
                int cmp = compare(keys.get(mid), key);
                if (cmp < 0) low = mid + 1; else if (cmp > 0) high = mid - 1; else return mid;
            }
            return ~low;
        }
