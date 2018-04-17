    @SuppressWarnings({ "ResultOfMethodCallIgnored" })
    public static void deleteDirectory(File file) throws IOException {
        if (file == null) {
            String message = Logging.getMessage("nullValue.FileIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        File[] fileList = file.listFiles();
        if (fileList != null) {
            List<File> childFiles = new ArrayList<File>();
            List<File> childDirs = new ArrayList<File>();
            for (File child : fileList) {
                if (child == null) continue;
                if (child.isDirectory()) childDirs.add(child); else childFiles.add(child);
            }
            for (File childFile : childFiles) {
                childFile.delete();
            }
            for (File childDir : childDirs) {
                deleteDirectory(childDir);
            }
        }
    }
