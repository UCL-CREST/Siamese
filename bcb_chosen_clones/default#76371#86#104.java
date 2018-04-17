    public static void deleteInstallationFiles(File path) {
        try {
            if (!path.exists()) {
                return;
            }
            if (!path.isDirectory()) {
                System.out.println("Delete: " + path.getCanonicalPath());
                path.delete();
            } else {
                File[] files = path.listFiles();
                for (int i = 0; i < files.length; ++i) {
                    deleteInstallationFiles(files[i]);
                }
                path.delete();
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
