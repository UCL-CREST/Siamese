    public void changeSData() {
        String[][] fTempData = new String[sData[0].length][sData.length];
        for (int i = 0; i < sData.length; i++) for (int j = 0; j < sData[0].length; j++) fTempData[j][i] = sData[i][j];
        sData = fTempData;
    }
