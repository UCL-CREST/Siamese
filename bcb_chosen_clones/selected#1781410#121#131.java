    @Ignore
    public static void deleteTestFiles(File dir) {
        assertTrue(ensureDirIsTest(dir));
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                deleteTestFiles(f);
            }
            f.delete();
        }
    }
