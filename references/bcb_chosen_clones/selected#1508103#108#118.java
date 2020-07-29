    public Room getRoom(Vector rooms, String roomID) {
        if (rooms.size() == 0) return null;
        int start = 0;
        int end = rooms.size() - 1;
        while (start <= end) {
            int mid = (end + start) / 2;
            int comp = ((Room) rooms.elementAt(mid)).roomID().compareToIgnoreCase(roomID);
            if (comp == 0) return (Room) rooms.elementAt(mid); else if (comp > 0) end = mid - 1; else start = mid + 1;
        }
        return null;
    }
