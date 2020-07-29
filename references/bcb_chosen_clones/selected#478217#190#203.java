    public void deleteRecursive(String fileName, boolean tryOnly) throws SQLException {
        fileName = translateFileName(fileName);
        if (FileUtils.isDirectory(fileName)) {
            String[] list = listFiles(fileName);
            for (int i = 0; list != null && i < list.length; i++) {
                deleteRecursive(list[i], tryOnly);
            }
        }
        if (tryOnly) {
            tryDelete(fileName);
        } else {
            delete(fileName);
        }
    }
