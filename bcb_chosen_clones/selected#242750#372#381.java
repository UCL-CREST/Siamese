    public boolean deleteAll(File f) {
        boolean b = true;
        if (!f.isDirectory()) {
            return f.delete();
        }
        for (File ff : f.listFiles()) {
            b = deleteAll(ff) && b;
        }
        return b;
    }
