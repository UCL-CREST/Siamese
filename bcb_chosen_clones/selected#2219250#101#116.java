    private int binsearch(int num) {
        int left = 0;
        int right = dividers.length - 1;
        int index = right - left / 2;
        while (left < right) {
            index = (right + left) / 2;
            if (num == dividers[index]) {
                return num;
            } else if (num > dividers[index]) {
                left = index + 1;
            } else if (num < dividers[index]) {
                right = index - 1;
            }
        }
        return dividers[left];
    }
