    public static void deleteFilesInDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] subfiles = directory.listFiles();
            for (int i = 0; i < subfiles.length; i++) {
                if (subfiles[i].isFile()) {
                    subfiles[i].delete();
                    Assert.assertFalse(subfiles[i].getAbsolutePath(), subfiles[i].exists());
                }
            }
        }
    }
