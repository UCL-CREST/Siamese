    public int locateIndex(int index) {
        int min = 0, max = m_indices.size() - 1;
        if (max == -1) {
            return -1;
        }
        while ((m_indices.get(min) <= index) && (m_indices.get(max) >= index)) {
            int current = (max + min) / 2;
            if (m_indices.get(current) > index) {
                max = current - 1;
            } else if (m_indices.get(current) < index) {
                min = current + 1;
            } else {
                return current;
            }
        }
        if (m_indices.get(max) < index) {
            return max;
        } else {
            return min - 1;
        }
    }
