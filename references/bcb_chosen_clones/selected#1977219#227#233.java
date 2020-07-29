    public static void deleteRecursively(File directory) {
        File[] children = directory.listFiles();
        if (children != null) {
            for (File child : children) if (child.isDirectory()) deleteRecursively(child); else deleteFile(child);
            if (!directory.delete()) throw new RuntimeException("Cannot delete directory " + directory);
        }
    }
