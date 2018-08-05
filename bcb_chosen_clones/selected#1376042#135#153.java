    public static void clearCache(final File rootDir) {
        try {
            if (rootDir.exists()) {
                File[] list = rootDir.listFiles();
                for (File aList : list) {
                    if (aList.isDirectory()) {
                        clearCache(aList);
                    } else {
                        aList.delete();
                    }
                }
            }
            rootDir.delete();
            System.out.println("Cache component " + rootDir.toString() + " cleared.");
        } catch (Throwable t) {
            System.out.println("Unable to clear " + rootDir.toString());
            t.printStackTrace();
        }
    }
