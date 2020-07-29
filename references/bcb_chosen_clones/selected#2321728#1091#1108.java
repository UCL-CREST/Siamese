    protected int getProperIndex(Room R) {
        if (properRooms.size() == 0) return -1;
        if (R.roomID().length() == 0) return 0;
        String roomID = R.roomID();
        synchronized (properRooms) {
            int start = 0;
            int end = properRooms.size() - 1;
            int mid = 0;
            while (start <= end) {
                mid = (end + start) / 2;
                int comp = properRooms.elementAt(mid).roomID().compareToIgnoreCase(roomID);
                if (comp == 0) return mid; else if (comp > 0) end = mid - 1; else start = mid + 1;
            }
            if (end < 0) return 0;
            if (start >= properRooms.size()) return properRooms.size() - 1;
            return mid;
        }
    }
