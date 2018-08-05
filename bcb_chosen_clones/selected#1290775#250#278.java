    public void setValues(float[][] newVal) throws RemoteException, VisADException {
        if (newVal == null) {
            throw new VisADException("Can't set table to null");
        }
        if (newVal.length >= 3 && newVal.length <= 4 && newVal[0].length > 4) {
            hasAlpha = newVal.length > 3;
            resolution = newVal[0].length;
        } else if (newVal[0].length >= 3 && newVal[0].length <= 4 && newVal.length > 4) {
            hasAlpha = newVal[0].length > 3;
            resolution = newVal.length;
            float[][] tmpVal = new float[hasAlpha ? 4 : 3][resolution];
            for (int i = 0; i < resolution; i++) {
                tmpVal[RED][i] = newVal[i][RED];
                tmpVal[GREEN][i] = newVal[i][GREEN];
                tmpVal[BLUE][i] = newVal[i][BLUE];
                if (hasAlpha) {
                    tmpVal[ALPHA][i] = newVal[i][ALPHA];
                }
            }
            newVal = tmpVal;
        } else {
            throw new VisADException("Cannot set table with dimensions [" + newVal.length + "][" + newVal[0].length + "]");
        }
        if (ctl == null) {
            ctl = new BaseColorControl(null, hasAlpha ? 4 : 3);
        }
        ctl.setTable(newVal);
        sendUpdate(0, resolution - 1);
    }
