    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
        File fileDirectory = new File(tempFilePath);
        if (fileDirectory.isDirectory() && fileDirectory.exists()) {
            for (File file : fileDirectory.listFiles()) {
                file.delete();
            }
        }
    }
