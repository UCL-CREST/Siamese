    private boolean archiveFileTo(String destination, String pathPrefix) {
        File destinationFile = prepareDestinationArchive(destination);
        if (destinationFile == null) {
            return false;
        }
        try {
            JarOutputStream outputStream = new JarOutputStream(new FileOutputStream(destinationFile));
            outputStream.putNextEntry(new ZipEntry(pathPrefix + getName()));
            outputStream.write(getFileAsBytes());
            outputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            Log.error(TAG, "Could not create JarOutputStream: " + e.getMessage());
        } catch (IOException e) {
            Log.error(TAG, "Could not create JarOutputStream: " + e.getMessage());
        }
        return false;
    }
