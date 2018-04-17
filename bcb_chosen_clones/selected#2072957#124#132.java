    public void deleteRecursive(File _file) throws IOException {
        if (_file.exists()) {
            if (_file.isDirectory()) {
                File[] files = _file.listFiles();
                for (int i = 0, l = files.length; i < l; ++i) deleteRecursive(files[i]);
            }
            _file.delete();
        }
    }
