    public int locateIndex(int index) {
        int min = 0;
        int max = m_Indices.length - 1;
        while (max >= min) {
            int current = (max + min) / 2;
            if (m_Indices[current] > index) {
                max = current - 1;
            } else if (m_Indices[current] < index) {
                min = current + 1;
            } else {
                return current;
            }
        }
        return max;
    }
