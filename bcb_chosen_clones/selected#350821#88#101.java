    public void sortIndexes() {
        int i, j, count;
        int t;
        count = m_ItemIndexes.length;
        for (i = 1; i < count; i++) {
            for (j = 0; j < count - i; j++) {
                if (m_ItemIndexes[j] > m_ItemIndexes[j + 1]) {
                    t = m_ItemIndexes[j];
                    m_ItemIndexes[j] = m_ItemIndexes[j + 1];
                    m_ItemIndexes[j + 1] = t;
                }
            }
        }
    }
