    public static Object[][] reverseObjArray(Object[][] obj) {
        int objRow = obj.length, objColumn = obj[0].length;
        Object[][] result = new Object[objColumn][objRow];
        for (int row = 0; row < result.length; row++) for (int column = 0; column < result[0].length; column++) result[row][column] = obj[column][row];
        return result;
    }
