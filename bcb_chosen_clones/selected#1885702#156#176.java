    private static int findSocketIndex(int localport, boolean match) {
        synchronized (SocketTable) {
            int l = 0;
            int r = SocketTable.size() - 1;
            while (l <= r) {
                int m = (l + r) / 2;
                int p = ((WSocket) SocketTable.elementAt(m)).localport;
                if (localport == p) {
                    return m;
                } else if (localport < p) {
                    r = m - 1;
                } else {
                    l = m + 1;
                }
            }
            if (match) {
                return -1;
            }
            return r + 1;
        }
    }
