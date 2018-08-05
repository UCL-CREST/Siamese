    private void sortMasters() {
        masterCounter = 0;
        for (int i = 0; i < maxID; i++) {
            if (users[i].getMasterPoints() > 0) {
                masterHandleList[masterCounter] = users[i].getHandle();
                masterPointsList[masterCounter] = users[i].getMasterPoints();
                masterCounter = masterCounter + 1;
            }
        }
        for (int i = masterCounter; --i >= 0; ) {
            for (int j = 0; j < i; j++) {
                if (masterPointsList[j] > masterPointsList[j + 1]) {
                    int tempp = masterPointsList[j];
                    String temppstring = masterHandleList[j];
                    masterPointsList[j] = masterPointsList[j + 1];
                    masterHandleList[j] = masterHandleList[j + 1];
                    masterPointsList[j + 1] = tempp;
                    masterHandleList[j + 1] = temppstring;
                }
            }
        }
    }
