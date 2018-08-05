    private void deleteDir(String strFile) {
        File fDir = new File(strFile);
        File[] strChildren = null;
        if (fDir.isDirectory()) {
            strChildren = fDir.listFiles();
            for (int i = 0; i < strChildren.length; i++) {
                strChildren[i].delete();
            }
        }
    }
