    public static void CopyFiles(File sourceFile, File destFile) {
        try {
            if (sourceFile.isDirectory()) {
                destFile.mkdir();
                String[] files = sourceFile.list();
                for (String fileName : files) {
                    File theSourceFile = new File(sourceFile + File.separator + fileName);
                    File theDestFile = new File(destFile + File.separator + fileName);
                    CopyFiles(theSourceFile, theDestFile);
                }
            } else {
                Files.copy(sourceFile, destFile);
            }
        } catch (IOException e) {
            Log.Write(e.getMessage());
        }
    }
