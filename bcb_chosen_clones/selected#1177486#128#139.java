    public static void copy(String from, String to) throws SourceException {
        File fromFile = new File(from);
        if (fromFile.isDirectory()) {
            File toDir = new File(to);
            if (!toDir.exists()) toDir.mkdir();
            for (File myFile : fromFile.listFiles()) {
                if (myFile.isDirectory()) copy(myFile.getAbsolutePath(), to + File.separator + myFile.getName()); else copyFile(myFile.getAbsolutePath(), to + File.separator + myFile.getName());
            }
        } else {
            copyFile(from, to);
        }
    }
