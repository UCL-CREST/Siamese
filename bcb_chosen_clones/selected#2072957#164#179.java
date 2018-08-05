    public void deleteRecursiveExcludePattern(File file, String[] excludePattern) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0, l = files.length; i < l; ++i) deleteRecursiveExcludePattern(files[i], excludePattern);
            }
            boolean delete = true;
            for (int x = 0; x < excludePattern.length; x++) {
                if (Pattern.matches(excludePattern[x], file.getName())) {
                    delete = false;
                    break;
                }
            }
            if (delete) file.delete();
        }
    }
