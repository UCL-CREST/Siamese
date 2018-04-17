    public int getPlayersIdx(String player) {
        if (player == null || player == "") return -1;
        int start = 0;
        int end = playersIdx - 1;
        int mid = end / 2;
        int found = -1;
        String s = "";
        while (start <= end && found == -1) {
            if ((s = players[mid].getPlayer()).equalsIgnoreCase(player)) {
                found = mid;
            } else if (player.compareToIgnoreCase(s) < 0) {
                end = mid - 1;
            } else {
                start = mid + 1;
            }
            mid = (start + end) / 2;
        }
        return found;
    }
