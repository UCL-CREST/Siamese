    private int findNearestPair(double key, double secondaryKey) {
        int low = 0;
        int high = m_NumValues;
        int middle = 0;
        while (low < high) {
            middle = (low + high) / 2;
            double current = m_CondValues[middle];
            if (current == key) {
                double secondary = m_Values[middle];
                if (secondary == secondaryKey) {
                    return middle;
                }
                if (secondary > secondaryKey) {
                    high = middle;
                } else if (secondary < secondaryKey) {
                    low = middle + 1;
                }
            }
            if (current > key) {
                high = middle;
            } else if (current < key) {
                low = middle + 1;
            }
        }
        return low;
    }
