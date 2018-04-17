    private void deltree(File d) {
        for (File f : d.listFiles()) {
            if (f.isDirectory()) deltree(f);
            try {
                f.delete();
            } catch (SecurityException x) {
                System.out.println(x);
            }
        }
        try {
            d.delete();
        } catch (SecurityException x) {
        }
    }
