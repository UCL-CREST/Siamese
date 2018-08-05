    public String fileUpload(final ResourceType type, final String currentFolder, final String fileName, final InputStream inputStream) throws InvalidCurrentFolderException, WriteException {
        String absolutePath = getRealUserFilesAbsolutePath(RequestCycleHandler.getUserFilesAbsolutePath(ThreadLocalData.getRequest()));
        File typeDir = getOrCreateResourceTypeDir(absolutePath, type);
        File currentDir = new File(typeDir, currentFolder);
        if (!currentDir.exists() || !currentDir.isDirectory()) throw new InvalidCurrentFolderException();
        File newFile = new File(currentDir, fileName);
        File fileToSave = UtilsFile.getUniqueFile(newFile.getAbsoluteFile());
        try {
            IOUtils.copyLarge(inputStream, new FileOutputStream(fileToSave));
        } catch (IOException e) {
            throw new WriteException();
        }
        return fileToSave.getName();
    }
