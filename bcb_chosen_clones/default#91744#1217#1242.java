    boolean deleteFileStructure(File oldFile) {
        if (oldFile == null) return false;
        if (oldFile.isDirectory()) {
            if (progressDialog != null) {
                progressDialog.setDetailFile(oldFile, ProgressDialog.DELETE);
            }
            File[] subFiles = oldFile.listFiles();
            if (subFiles != null) {
                if (progressDialog != null) {
                    progressDialog.addWorkUnits(subFiles.length);
                }
                for (int i = 0; i < subFiles.length; i++) {
                    File oldSubFile = subFiles[i];
                    if (!deleteFileStructure(oldSubFile)) return false;
                    if (progressDialog != null) {
                        progressDialog.addProgress(1);
                        if (progressDialog.isCancelled()) return false;
                    }
                }
            }
        }
        if (simulateOnly) {
            return true;
        }
        return oldFile.delete();
    }
