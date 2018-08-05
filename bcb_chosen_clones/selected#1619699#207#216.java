    public int lastIndexBeforeY(int y) {
        int low = 0;
        int high = list.size() - 1;
        int index = -1;
        while (low <= high) {
            index = (low + high) / 2;
            if (list.get(index).getY() < y) low = index + 1; else if (list.get(index).getY() > y) high = index - 1; else break;
        }
        return (list.get(index).getY() > y) ? index - 1 : index;
    }
