    private int nearBinarySearch(Object[] data, int startIndex, int endIndex, Object itemToFind) {
        Comparer order = this.order;
        int lowIndex = startIndex;
        int highIndex = endIndex;
        while ((highIndex - lowIndex) > 3) {
            int midPoint = (highIndex + lowIndex) / 2;
            if (order.lessThan(itemToFind, data[midPoint])) highIndex = midPoint - 1; else if (order.greaterThan(itemToFind, data[midPoint])) lowIndex = midPoint + 1; else return midPoint;
        }
        return highIndex;
    }
