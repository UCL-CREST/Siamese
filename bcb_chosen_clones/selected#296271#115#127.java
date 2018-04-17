    public void restoreBackup(File backupFile, File destDir, boolean replaceExistingFile) throws BackupException, FileAlreadyExistsException {
        try {
            File databaseEncryptionFile = new File(destDir.getPath() + File.separator + getDatabase() + DATABASE_FILE_END);
            if (databaseEncryptionFile.exists() == true && replaceExistingFile == false) {
                throw new FileAlreadyExistsException("File " + databaseEncryptionFile.getName() + "already exists");
            }
            IOUtils.copy(FileUtils.openInputStream(backupFile), FileUtils.openOutputStream(databaseEncryptionFile));
        } catch (FileNotFoundException e) {
            throw new BackupException(e);
        } catch (IOException e) {
            throw new BackupException(e);
        }
    }
