    private void assertComparatorYieldsOrder(String[] orderedObjects, Comparator<String> comparator) {
        String[] keys = (String[]) orderedObjects.clone();
        boolean isInNewOrder = false;
        while (keys.length > 1 && isInNewOrder == false) {
            shuffle: {
                Random rand = new Random();
                for (int i = keys.length - 1; i > 0; i--) {
                    String swap = keys[i];
                    int j = rand.nextInt(i + 1);
                    keys[i] = keys[j];
                    keys[j] = swap;
                }
            }
            testShuffle: {
                for (int i = 0; i < keys.length && !isInNewOrder; i++) {
                    if (!orderedObjects[i].equals(keys[i])) {
                        isInNewOrder = true;
                    }
                }
            }
        }
        Arrays.sort(keys, comparator);
        for (int i = 0; i < orderedObjects.length; i++) {
            assertEquals(orderedObjects[i], keys[i]);
        }
    }
