    private void delete(File dir) {
        File[] files = dir.listFiles();
        if (files != null) for (File file : files) {
            if (file.isDirectory()) delete(file);
            file.delete();
        }
    }
