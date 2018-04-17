    private void clearData(String path) {
        String dataPath = path + "/db";
        File dir = new File(dataPath);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (int i = 0, m = files.length; i < m; i++) {
                    if (files[i].isDirectory()) {
                        clearDir(files[i].getAbsolutePath());
                    }
                    files[i].delete();
                }
            }
        } else {
            dir.mkdir();
        }
    }
