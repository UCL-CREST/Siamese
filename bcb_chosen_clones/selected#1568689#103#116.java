    private static int binarySearchAlbum(Vector<MusicOutputDesign> v, int low, int high) {
        if (high < low) {
            return -1;
        }
        int mid = (high + low) / 2;
        String s = v.elementAt(mid).getAlbum().replace("\\", "/");
        if (s.compareToIgnoreCase(artist) > 0) {
            return binarySearchAlbum(v, low, mid - 1);
        } else if (s.compareToIgnoreCase(artist) < 0) {
            return binarySearchAlbum(v, mid + 1, high);
        } else {
            return mid;
        }
    }
