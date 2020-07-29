    public void copyDirectory(File topLevelDirectory, File sourceDirectory, File destinationDirectory) throws IOException {
        if (topLevelDirectory == null) topLevelDirectory = sourceDirectory;
        if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) throw new IOException("No such source directory: " + sourceDirectory.getAbsolutePath());
        if (destinationDirectory.exists()) {
            if (!destinationDirectory.isDirectory()) throw new IOException("Destination exists but is not a directory: " + sourceDirectory.getAbsolutePath());
        } else destinationDirectory.mkdirs();
        File[] files = sourceDirectory.listFiles();
        for (File file : files) {
            File destinationFile = new File(destinationDirectory, file.getName());
            if (file.isDirectory()) copyDirectory(topLevelDirectory, file, destinationFile); else {
                String digest = copyFile(file, destinationFile);
                if (digest != null) {
                    String relativePath = getRelativePath(topLevelDirectory, file);
                    allPlatformFiles.put(topLevelDirectory, relativePath);
                    if (!allDigestsByRelativePath.containsKey(relativePath)) allDigestsByRelativePath.put(relativePath, digest);
                    if (allDigestsByRelativePath.get(relativePath).equals(digest)) {
                        allDigestsByRelativePath.put(relativePath, digest);
                        if (!relativeFileOccurencies.containsKey(relativePath)) relativeFileOccurencies.put(relativePath, 1); else relativeFileOccurencies.put(relativePath, relativeFileOccurencies.get(relativePath) + 1);
                    }
                }
            }
        }
    }
