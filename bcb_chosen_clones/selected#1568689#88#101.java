    private static int binarySearchArtist(Vector<MusicOutputDesign> v, int low, int high) {
        if (high < low) {
            return -1;
        }
        int mid = (high + low) / 2;
        String s = v.elementAt(mid).getArtist().replace("\\", "/");
        if (s.compareToIgnoreCase(artist) > 0) {
            return binarySearchArtist(v, low, mid - 1);
        } else if (s.compareToIgnoreCase(artist) < 0) {
            return binarySearchArtist(v, mid + 1, high);
        } else {
            return mid;
        }
    }
