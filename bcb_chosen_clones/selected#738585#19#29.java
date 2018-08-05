    public Permutator(Object[] e) {
        size = e.length;
        elements = new Object[size];
        System.arraycopy(e, 0, elements, 0, size);
        ar = Array.newInstance(e.getClass().getComponentType(), size);
        System.arraycopy(e, 0, ar, 0, size);
        permutation = new int[size + 1];
        for (int i = 0; i < size + 1; i++) {
            permutation[i] = i;
        }
    }
