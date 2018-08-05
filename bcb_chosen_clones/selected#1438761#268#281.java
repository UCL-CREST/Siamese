    private static int binarySearchAlbum(Vector<MusicOutputDesign> v, String value, int low, int high) {
        if (high < low) {
            return -1;
        }
        int mid = (high + low) / 2;
        String s = v.elementAt(mid).getAlbum().replace("\\", "/");
        if (s.compareToIgnoreCase(value) > 0) {
            return binarySearchArtist(v, value, low, mid - 1);
        } else if (s.compareToIgnoreCase(value) < 0) {
            return binarySearchArtist(v, value, mid + 1, high);
        } else {
            return mid;
        }
    }
