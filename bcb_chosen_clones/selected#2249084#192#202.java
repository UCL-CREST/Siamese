    public int binary_search(int idx, char size, char key) {
        int left = 0, right, middle;
        ST_NODE node;
        right = (int) size - 1;
        while (left <= right) {
            middle = (left + right) / 2;
            node = this.nf[middle + idx].node;
            if (key > node.K) left = middle + 1; else if (key < node.K) right = middle - 1; else return (idx + middle);
        }
        return 0;
    }
