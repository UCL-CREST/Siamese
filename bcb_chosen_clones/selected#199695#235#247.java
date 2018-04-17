    protected int search(String num) {
        String[] strings = this.strings;
        int lb = 0, ub = string_count, index;
        String index_key;
        while (true) {
            if (lb >= ub - 1) {
                if (lb < string_count && !greaterThan(num, strings[lb])) return lb; else return lb + 1;
            }
            index = (lb + ub) / 2;
            index_key = strings[index];
            if (greaterThan(num, index_key)) lb = index + 1; else if (lessThan(num, index_key)) ub = index; else return index;
        }
    }
