    public static boolean makeEmpty(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return true;
        }
        boolean retVal = true;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                retVal &= makeEmpty(file);
            }
            retVal &= file.delete();
        }
        return retVal;
    }
