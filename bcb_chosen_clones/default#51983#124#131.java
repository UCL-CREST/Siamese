    static boolean clean(File dir) {
        boolean ok = true;
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) ok &= clean(f);
            ok &= f.delete();
        }
        return ok;
    }
