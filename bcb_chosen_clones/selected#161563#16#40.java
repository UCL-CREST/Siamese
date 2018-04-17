    public static String[] bubbleSort(String[] unsortedString, boolean ascending) {
        if (unsortedString.length < 2) return unsortedString;
        String[] sortedString = new String[unsortedString.length];
        for (int i = 0; i < unsortedString.length; i++) {
            sortedString[i] = unsortedString[i];
        }
        if (ascending) {
            for (int i = 0; i < sortedString.length - 1; i++) {
                for (int j = 1; j < sortedString.length - 1 - i; j++) if (sortedString[j + 1].compareToIgnoreCase(sortedString[j]) < 0) {
                    String swap = sortedString[j];
                    sortedString[j] = sortedString[j + 1];
                    sortedString[j + 1] = swap;
                }
            }
        } else {
            for (int i = sortedString.length - 2; i >= 0; i--) {
                for (int j = sortedString.length - 2 - i; j >= 0; j--) if (sortedString[j + 1].compareToIgnoreCase(sortedString[j]) > 0) {
                    String swap = sortedString[j];
                    sortedString[j] = sortedString[j + 1];
                    sortedString[j + 1] = swap;
                }
            }
        }
        return sortedString;
    }
