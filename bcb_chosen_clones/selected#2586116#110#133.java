    public static boolean deleteDirectory(File directory, int pathSize) {
        if (directory != null && directory.isDirectory()) {
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.getPath().length() > pathSize) {
                    if (file.isDirectory()) {
                        boolean success = deleteDirectory(file, file.getPath().length());
                        if (!success) {
                            return false;
                        }
                    } else {
                        boolean success = file.delete();
                        log.trace("Trying to delete: " + file.getName() + "?: " + success);
                        if (!success) {
                            return false;
                        }
                    }
                } else log.warn("Stopped deleting " + directory.getAbsolutePath() + ", detected a link to smaller path size: " + file.getPath());
            }
            return directory.delete();
        }
        return directory.delete();
    }
