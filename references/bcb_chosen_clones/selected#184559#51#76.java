    private int findNearestPair(double key, double secondaryKey) {
        int low = 0;
        int high = m_CondValues.size();
        int middle = 0;
        while (low < high) {
            middle = (low + high) / 2;
            double current = ((Double) m_CondValues.elementAt(middle)).doubleValue();
            if (current == key) {
                double secondary = ((Double) m_Values.elementAt(middle)).doubleValue();
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
