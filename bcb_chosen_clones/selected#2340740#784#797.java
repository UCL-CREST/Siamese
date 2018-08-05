    private void dumpFile(File repository, File copy) {
        try {
            if (copy.exists() && !copy.delete()) {
                throw new RuntimeException("can't delete copy: " + copy);
            }
            printFile("Real Archive File", repository);
            new ZipArchive(repository.getPath());
            IOUtils.copyFiles(repository, copy);
            printFile("Copy Archive File", copy);
            new ZipArchive(copy.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
