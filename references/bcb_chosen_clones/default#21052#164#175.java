    public void bubblesort(String filenames[]) {
        for (int i = filenames.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                String temp;
                if (filenames[j].compareTo(filenames[j + 1]) > 0) {
                    temp = filenames[j];
                    filenames[j] = filenames[j + 1];
                    filenames[j + 1] = temp;
                }
            }
        }
    }
