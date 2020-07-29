    public void add(String str) {
        String areaName = str.toUpperCase().trim();
        if (areaName.length() == 0) return;
        String theRest = null;
        long roomNum = -1;
        int x = areaName.indexOf("#");
        if (x > 0) {
            theRest = areaName.substring(x + 1).trim();
            areaName = areaName.substring(0, x);
            x = theRest.indexOf("#(");
            if ((x >= 0) && (theRest.endsWith(")")) && (CMath.isInteger(theRest.substring(0, x)))) {
                int comma = theRest.indexOf(",", x);
                if (comma > 0) {
                    roomNum = (Long.parseLong(theRest.substring(0, x)) << 30);
                    roomNum += (Long.parseLong(theRest.substring(x + 2, comma)) << 15);
                    roomNum += Long.parseLong(theRest.substring(comma + 1, theRest.length() - 1));
                    if (roomNum < CMIntegerGrouper.NEXT_BITS) roomNum |= CMIntegerGrouper.GRID_FLAGL;
                }
            } else if (CMath.isInteger(theRest)) roomNum = Integer.parseInt(theRest.substring(x + 1).trim());
        }
        int start = 0;
        int end = root.size() - 1;
        int comp = -1;
        int mid = -1;
        int lastStart = 0;
        int lastEnd = root.size() - 1;
        while (start <= end) {
            mid = (end + start) / 2;
            comp = areaName.compareTo((String) root.elementAt(mid, 1));
            if (comp == 0) break; else if (comp < 0) {
                lastEnd = end;
                end = mid - 1;
            } else {
                lastStart = start;
                start = mid + 1;
            }
        }
        if (comp == 0) {
            if (root.elementAt(mid, 2) != null) ((CMIntegerGrouper) root.elementAt(mid, 2)).add(roomNum);
        } else {
            if (mid < 0) root.addElement(areaName, ((CMIntegerGrouper) CMClass.getCommon("DefaultCMIntegerGrouper")).add(roomNum)); else {
                for (comp = lastStart; comp <= lastEnd; comp++) if (areaName.compareTo((String) root.elementAt(comp, 1)) < 0) {
                    root.insertElementAt(comp, areaName, ((CMIntegerGrouper) CMClass.getCommon("DefaultCMIntegerGrouper")).add(roomNum));
                    return;
                }
                root.addElement(areaName, ((CMIntegerGrouper) CMClass.getCommon("DefaultCMIntegerGrouper")).add(roomNum));
            }
        }
    }
