    public static int getIdNameIndexSort(final FudaaExec[] _ex, final String _idName) {
        if (_idName == null) {
            return -1;
        }
        int lowIndex = 0;
        int highIndex = _ex.length;
        int temp, tempMid;
        while (lowIndex <= highIndex) {
            tempMid = (lowIndex + highIndex) / 2;
            temp = _ex[tempMid].getIDName().compareTo(_idName);
            if (temp < 0) {
                lowIndex = tempMid + 1;
            } else if (temp > 0) {
                highIndex = tempMid - 1;
            } else {
                return tempMid;
            }
        }
        return -1;
    }
