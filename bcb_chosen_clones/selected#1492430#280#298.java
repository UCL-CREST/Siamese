    public static <T> T[] addWithoutDuplicates(T[] values, T[] newValues) {
        Set<T> originals = new HashSet<T>(values.length);
        for (T value : values) {
            originals.add(value);
        }
        List<T> newOnes = new ArrayList<T>(newValues.length);
        for (T value : newValues) {
            if (originals.contains(value)) {
                continue;
            }
            newOnes.add(value);
        }
        T[] largerOne = (T[]) Array.newInstance(values.getClass().getComponentType(), values.length + newOnes.size());
        System.arraycopy(values, 0, largerOne, 0, values.length);
        for (int i = values.length; i < largerOne.length; i++) {
            largerOne[i] = newOnes.get(i - values.length);
        }
        return largerOne;
    }
