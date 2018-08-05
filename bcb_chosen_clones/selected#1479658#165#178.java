    private boolean deleteDir(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                if (!deleteDir(child)) {
                    return false;
                }
            }
        }
        boolean successful = file.delete();
        if (!successful) {
            log().warn("Failed to delete file: " + file.getAbsolutePath());
        }
        return successful;
    }
