    public static void deleteFiles(final File file, final String... stringPatterns) {
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles != null && childFiles.length > 0) {
                for (File childFile : childFiles) {
                    deleteFiles(childFile, stringPatterns);
                }
            }
        }
        Pattern pattern = getPattern(stringPatterns);
        if (pattern.matcher(file.getName()).matches()) {
            FileUtilities.deleteFile(file, 1);
        }
    }
