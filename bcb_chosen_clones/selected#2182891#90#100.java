    protected void doDel(File f) {
        if (f.isDirectory()) {
            File[] ls = f.listFiles();
            for (File element : ls) {
                doDel(element);
            }
            f.delete();
        } else {
            f.delete();
        }
    }
