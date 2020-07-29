    private File newFile(File oldFile) throws IOException {
        int counter = 0;
        File nFile = new File(this.stateDirProvider.get() + File.separator + oldFile.getName());
        while (nFile.exists()) {
            nFile = new File(this.stateDirProvider.get() + File.separator + oldFile.getName() + "_" + counter);
        }
        IOUtils.copyFile(oldFile, nFile);
        return nFile;
    }
