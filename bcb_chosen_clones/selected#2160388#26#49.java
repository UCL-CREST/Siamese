    @SuppressWarnings("unchecked")
    public void copyStorageFiles(Map<String, String> envVars, File vmStorageDir) throws IOException {
        String storagePath = envVars.get(WorkerConstants.ENV_STORAGE);
        if (storagePath == null) {
            return;
        }
        File storageDir = new File(storagePath);
        if (!storageDir.isDirectory()) {
            return;
        }
        Collection<File> storageFiles = FileUtils.listFiles(storageDir, null, true);
        LOG.debug("Storage path: " + storagePath);
        StringBuffer sb = new StringBuffer("Storage files:\n");
        for (File file : storageFiles) {
            sb.append(file.getAbsolutePath() + "\n");
        }
        LOG.debug(sb.toString());
        for (File file : storageFiles) {
            String destination = file.getAbsolutePath().replace(storagePath, "");
            File vmStoredFile = new File(vmStorageDir + File.separator + destination);
            FileUtils.copyFile(file, vmStoredFile);
            LOG.debug("Held stored file copied to: " + vmStoredFile + " vm storage directory ");
        }
    }
