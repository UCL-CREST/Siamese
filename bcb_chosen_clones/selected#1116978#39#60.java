    public static void delete(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                if (!file.delete()) {
                    Assert.fail("Could not delete file: " + file.getPath());
                }
            } else {
                for (File entry : file.listFiles()) {
                    if (entry.isDirectory()) {
                        delete(entry);
                    } else {
                        if (!entry.delete()) {
                            Assert.fail("Could not delete file: " + entry.getPath());
                        }
                    }
                }
                if (!file.delete()) {
                    Assert.fail("Could not delete directory: " + file.getPath());
                }
            }
        }
    }
