    public void deleteRecursiveWithExclusion(File file, String excludeName) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0, l = files.length; i < l; ++i) deleteRecursiveWithExclusion(files[i], excludeName);
            }
            if (!file.getName().startsWith(excludeName)) {
                file.delete();
            }
        }
    }
