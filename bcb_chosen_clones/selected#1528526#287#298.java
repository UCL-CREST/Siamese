    private synchronized Boolean removeFile(File file) {
        Boolean retVal = false;
        if ((file != null) && (file.exists())) {
            if (file.isDirectory()) {
                for (File subFile : file.listFiles()) {
                    removeFile(subFile);
                }
            }
            retVal = file.delete();
        }
        return (retVal);
    }
