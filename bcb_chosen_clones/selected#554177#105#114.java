    public static void deleteFileRecursively(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            File[] subfiles = fileOrDirectory.listFiles();
            for (int i = 0; i < subfiles.length; i++) {
                deleteFileRecursively(subfiles[i]);
            }
        }
        fileOrDirectory.delete();
        Assert.assertFalse(fileOrDirectory.getAbsolutePath(), fileOrDirectory.exists());
    }
