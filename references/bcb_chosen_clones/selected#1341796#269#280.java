    private boolean deleteAll(File delFile) {
        if (delFile.exists()) {
            for (File f : delFile.listFiles()) {
                if (f.isDirectory()) {
                    deleteAll(f);
                } else {
                    f.delete();
                }
            }
        }
        return delFile.delete();
    }
