    private void removeDir(final File dir) {
        final File[] files = dir.listFiles();
        if (files != null) {
            for (final File f : files) {
                if (f.isDirectory()) {
                    removeDir(f);
                } else {
                    f.delete();
                }
            }
        }
        dir.delete();
    }
