    public static void removeFiles(File envFile) {
        File[] targetFiles = envFile.listFiles();
        for (int i = 0; i < targetFiles.length; i++) {
            File f = targetFiles[i];
            if (f.isDirectory() || f.getName().equals("je.properties")) {
                continue;
            }
            boolean done = targetFiles[i].delete();
            if (!done) {
                System.out.println("Warning, couldn't delete " + targetFiles[i] + " out of " + targetFiles[targetFiles.length - 1]);
            }
        }
    }
