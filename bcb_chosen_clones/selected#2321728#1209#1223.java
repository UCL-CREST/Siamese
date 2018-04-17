    public Room getRoom(String roomID) {
        if (properRooms.size() == 0) return null;
        if (roomID.length() == 0) return null;
        synchronized (properRooms) {
            if (roomID.toUpperCase().startsWith(Name().toUpperCase() + "#")) roomID = Name() + roomID.substring(Name().length());
            int start = 0;
            int end = properRooms.size() - 1;
            while (start <= end) {
                int mid = (end + start) / 2;
                int comp = properRooms.elementAt(mid).roomID().compareToIgnoreCase(roomID);
                if (comp == 0) return properRooms.elementAt(mid); else if (comp > 0) end = mid - 1; else start = mid + 1;
            }
        }
        return null;
    }
