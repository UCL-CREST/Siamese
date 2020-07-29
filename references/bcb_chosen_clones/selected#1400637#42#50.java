    private void deleteDirectory(File dir) {
        File[] fileArray = dir.listFiles();
        if (fileArray != null) {
            for (int i = 0; i < fileArray.length; i++) {
                if (fileArray[i].isDirectory()) deleteDirectory(fileArray[i]); else fileArray[i].delete();
            }
        }
        dir.delete();
    }
