    public int lastIndexBeforeX(int x) {
        int low = 0;
        int high = list.size() - 1;
        int index = -1;
        while (low <= high) {
            index = (low + high) / 2;
            if (list.get(index).getX() < x) low = index + 1; else if (list.get(index).getX() > x) high = index - 1; else break;
        }
        return (list.get(index).getX() > x) ? index - 1 : index;
    }
