    public static int[] simplex_reverse_sort(int[] vertices) {
        if (vertices.length <= 1) return vertices;
        for (int j = vertices.length - 1; j > 0; j--) {
            for (int i = 0; i < j; i++) {
                if (vertices[i + 1] > vertices[i]) {
                    int dummy = vertices[i];
                    vertices[i] = vertices[i + 1];
                    vertices[i + 1] = dummy;
                }
            }
        }
        return vertices;
    }
