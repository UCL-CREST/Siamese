    private final void reOrderFriendsListByOnlineStatus() {
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = 0; i < friendsCount - 1; i++) if (friendsListOnlineStatus[i] < friendsListOnlineStatus[i + 1]) {
                int j = friendsListOnlineStatus[i];
                friendsListOnlineStatus[i] = friendsListOnlineStatus[i + 1];
                friendsListOnlineStatus[i + 1] = j;
                long l = friendsListLongs[i];
                friendsListLongs[i] = friendsListLongs[i + 1];
                friendsListLongs[i + 1] = l;
                flag = true;
            }
        }
    }
