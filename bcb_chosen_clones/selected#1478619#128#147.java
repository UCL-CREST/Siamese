    private int properRoomIndex(int x, int y) {
        if (rooms.size() == 0) return 0;
        synchronized (rooms) {
            int start = 0;
            int end = rooms.size() - 1;
            int comp = 0;
            long total = ((long) x << 31) + y;
            long comptotal = 0;
            int mid = 0;
            while (start <= end) {
                mid = (end + start) / 2;
                comptotal = (((Integer) rooms.elementAt(mid, 2)).longValue() << 31) + ((Integer) rooms.elementAt(mid, 3)).longValue();
                comp = comptotal > total ? 1 : (comptotal == total) ? 0 : -1;
                if (comp == 0) return mid; else if (comp > 0) end = mid - 1; else start = mid + 1;
            }
            if (end < 0) return 0;
            if (start >= rooms.size()) return rooms.size() - 1;
            return mid;
        }
    }
