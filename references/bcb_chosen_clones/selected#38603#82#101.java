    public static void copyDirectory(File source, File destination, String endWith) throws IOException {
        if (source.exists() && source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }
            File[] fileArray = source.listFiles();
            for (int i = 0; i < fileArray.length; i++) {
                if (fileArray[i].isDirectory()) {
                    copyDirectory(fileArray[i], new File(destination.getPath() + File.separator + fileArray[i].getName()), endWith);
                } else {
                    if (endWith != null) {
                        if (!fileArray[i].getPath().toLowerCase().endsWith(endWith.toLowerCase())) {
                            continue;
                        }
                    }
                    copyFile(fileArray[i], new File(destination.getPath() + File.separator + fileArray[i].getName()));
                }
            }
        }
    }
