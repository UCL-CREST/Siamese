    private static boolean deleteRecursive(File dir) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                if (!deleteRecursive(file)) return false;
            } else {
                if (file.delete()) {
                } else {
                    System.out.printf("Unable to delete the file %s\n", file.getAbsoluteFile());
                    return false;
                }
            }
        }
        if (!dir.delete()) {
            System.out.println("Unable to delete directory " + dir.getAbsolutePath());
        } else {
        }
        return true;
    }
