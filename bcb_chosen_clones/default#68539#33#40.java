    private static void delete(File f) {
        File[] fs = f.listFiles();
        for (int i = 0; i < fs.length; i++) {
            System.out.println("Deleting: " + fs[i].getPath());
            if (fs[i].isDirectory()) delete(fs[i]);
            fs[i].delete();
        }
    }
