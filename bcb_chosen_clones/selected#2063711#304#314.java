    private void outputCoordinates() {
        for (int i = 0; i < landmarks.length; i++) for (int j = 0; j < i; j++) {
            delays[i][j] = (delays[i][j] + delays[j][i]) / 2;
            delays[j][i] = delays[i][j];
        }
        GNP_Optimization opt = new GNP_Optimization(delays, 2);
        float[][] coor = opt.getLMCoordinates();
        for (int i = 0; i < landmarks.length; i++) {
            System.out.println(PROPERTY_PROTO_PREFIX + ".USE_LM.Landmark" + i + ".Coordinates = " + coor[i][0] + "," + coor[i][1]);
        }
    }
