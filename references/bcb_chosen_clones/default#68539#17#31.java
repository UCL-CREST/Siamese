    private static void traverse(File f) {
        File[] fs = f.listFiles();
        for (int i = 0; i < fs.length; i++) {
            if (fs[i].getName().equals("CVS") || fs[i].getName().equals(".svn")) {
                delete(fs[i]);
                fs[i].delete();
                System.out.println("Deleting dir: " + fs[i].getPath());
            } else if (fs[i].isDirectory()) {
                traverse(fs[i]);
            } else if (fs[i].getName().endsWith("~") || fs[i].getName().startsWith(".#")) {
                System.out.println("Deleting: " + fs[i].getPath());
                fs[i].delete();
            }
        }
    }
