    private static boolean deleteFile(File resource) throws IOException {
        if (resource.isDirectory()) {
            File[] childFiles = resource.listFiles();
            for (File child : childFiles) {
                deleteFile(child);
            }
        }
        return resource.delete();
    }
