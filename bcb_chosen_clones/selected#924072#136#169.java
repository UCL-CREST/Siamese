    private boolean addToSortedList(Object element) {
        int min = 0;
        int max = size() - 1;
        boolean found = false;
        int currentIndex = 0;
        int compareResult;
        if (max >= 0) {
            do {
                currentIndex = (min + max) / 2;
                compareResult = ((Comparable) myList.get(currentIndex)).compareTo(element);
                if (compareResult < 0) {
                    min = currentIndex + 1;
                } else if (compareResult > 0) {
                    max = currentIndex - 1;
                } else {
                    found = true;
                }
            } while ((min <= max) && (found == false));
        }
        if (found == false && size() > 0) {
            if (((Comparable) element).compareTo(get(currentIndex)) > 0) {
                myList.add(currentIndex + 1, element);
            } else {
                myList.add(currentIndex, element);
            }
            return true;
        } else if (found == false) {
            myList.add(currentIndex, element);
            return true;
        } else {
            System.out.println("Element found in vector already.");
            return false;
        }
    }
