    protected void addRoom(Vector rooms, Room R) {
        try {
            String roomID = R.roomID();
            int start = 0;
            int end = rooms.size() - 1;
            int lastStart = 0;
            int lastEnd = rooms.size() - 1;
            int comp = -1;
            int mid = -1;
            while (start <= end) {
                mid = (end + start) / 2;
                comp = ((Room) rooms.elementAt(mid)).roomID().compareToIgnoreCase(roomID);
                if (comp == 0) break; else if (comp > 0) {
                    lastEnd = end;
                    end = mid - 1;
                } else {
                    lastStart = start;
                    start = mid + 1;
                }
            }
            if (comp == 0) rooms.setElementAt(R, mid); else {
                if (mid >= 0) for (comp = lastStart; comp <= lastEnd; comp++) if (((Room) rooms.elementAt(comp)).roomID().compareToIgnoreCase(roomID) > 0) {
                    rooms.insertElementAt(R, comp);
                    return;
                }
                rooms.addElement(R);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
