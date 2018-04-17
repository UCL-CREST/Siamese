    private boolean archiveDirectoryTo(String destination, String pathPrefix) {
        File destinationFile = prepareDestinationArchive(destination);
        if (destinationFile == null) {
            return false;
        }
        JarOutputStream outputStream;
        try {
            outputStream = new JarOutputStream(new FileOutputStream(destinationFile));
        } catch (FileNotFoundException e) {
            Log.error(TAG, "Could not create JarOutputStream: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.error(TAG, "Could not create JarOutputStream: " + e.getMessage());
            return false;
        }
        String basePath = getAbsolutePath();
        UniversalFile[] filesToArchive = listFilesRecursively();
        try {
            for (UniversalFile fileToArchive : filesToArchive) {
                String path = fileToArchive.getAbsolutePath();
                if (!path.startsWith(basePath)) {
                    Log.error(TAG, "Internal error: File in directory has wrong path:");
                    Log.error(TAG, "Base path: " + basePath);
                    Log.error(TAG, "File path: " + path);
                    return false;
                }
                path = path.substring(basePath.length() + 1);
                String entryPath = (pathPrefix + path).replace('\\', '/');
                outputStream.putNextEntry(new ZipEntry(entryPath));
                outputStream.write(fileToArchive.getFileAsBytes());
            }
            outputStream.close();
            return true;
        } catch (IOException e) {
            Log.error(TAG, "Could not write to archive: " + e.getMessage());
        }
        return false;
    }
