    public static boolean emptyFolder(final File folder) {
        if (!folder.isDirectory()) {
            return true;
        }
        File[] files = folder.listFiles();
        boolean result = true;
        for (File file : files) {
            if (file.isDirectory()) {
                if (emptyFolder(file)) {
                    result &= file.delete();
                } else {
                    result = false;
                }
            } else {
                result &= file.delete();
            }
        }
        return result;
    }
