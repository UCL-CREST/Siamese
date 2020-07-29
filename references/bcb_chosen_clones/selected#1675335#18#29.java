    public static String[][] dimTransform(String[][] obj) {
        if ((obj == null) || (obj.length <= 0)) {
            return null;
        }
        String[][] newArr = new String[obj[0].length][obj.length];
        for (int i = 0; i < newArr.length; ++i) {
            for (int j = 0; j < obj.length; ++j) {
                newArr[i][j] = obj[j][i];
            }
        }
        return newArr;
    }
