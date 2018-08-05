    public final double findIntervall(double conf) {
        if (conf == 1.0) return m_midPoints[m_midPoints.length - 1];
        int end = m_midPoints.length - 1;
        int start = 0;
        while (Math.abs(end - start) > 1) {
            int mid = (start + end) / 2;
            if (conf > m_midPoints[mid]) start = mid + 1;
            if (conf < m_midPoints[mid]) end = mid - 1;
            if (conf == m_midPoints[mid]) return m_midPoints[mid];
        }
        if (Math.abs(conf - m_midPoints[start]) <= Math.abs(conf - m_midPoints[end])) return m_midPoints[start]; else return m_midPoints[end];
    }
