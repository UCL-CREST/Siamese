    static void delete(File f) {
        if (f.isDirectory()) {
            if (f.listFiles().length > 0) {
                for (File c : f.listFiles()) {
                    delete(c);
                }
                f.delete();
            } else {
                f.delete();
            }
        } else {
            f.delete();
        }
    }
