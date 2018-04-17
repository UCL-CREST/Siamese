    public int locateIndex(int index) {
        int min = 0, max = m_Indices.length - 1;
        if (max == -1) {
            return -1;
        }
        while ((m_Indices[min] <= index) && (m_Indices[max] >= index)) {
            int current = (max + min) / 2;
            if (m_Indices[current] > index) {
                max = current - 1;
            } else if (m_Indices[current] < index) {
                min = current + 1;
            } else {
                return current;
            }
        }
        if (m_Indices[max] < index) {
            return max;
        } else {
            return min - 1;
        }
    }
