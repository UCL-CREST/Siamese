    public static boolean recursiveDelete(File d) {
        if (d.isDirectory()) {
            File children[] = d.listFiles();
            for (File f : children) {
                Assert.assertTrue("delete " + f.toString(), recursiveDelete(f));
            }
        }
        return d.delete();
    }
