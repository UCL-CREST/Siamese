    public static boolean delete(File file, StringBuffer errorLog) {
        boolean result = true;
        if (file.isDirectory()) {
            File[] fileDirContent = file.listFiles();
            if (file.list() != null && file.list().length > 0) {
                for (int i = 0; i < fileDirContent.length; i++) result = result & delete(fileDirContent[i], errorLog);
            }
            result = file.delete();
        } else {
            result = file.delete();
        }
        return result;
    }
