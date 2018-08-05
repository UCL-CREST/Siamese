    public static void copyDirectory(File source, File destination) {
        if (source.exists() && source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }
            File[] fileArray = source.listFiles();
            for (int i = 0; i < fileArray.length; i++) {
                if (fileArray[i].isDirectory()) {
                    copyDirectory(fileArray[i], new File(destination.getPath() + File.separator + fileArray[i].getName()));
                } else {
                    copyFile(fileArray[i], new File(destination.getPath() + File.separator + fileArray[i].getName()));
                }
            }
        }
    }
