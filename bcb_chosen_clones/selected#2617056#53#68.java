    public static void copyDir(File sourceDir, File destDir) {
        if (!sourceDir.exists()) return;
        if (!destDir.exists()) return;
        File file = new File(destDir, sourceDir.getName());
        if (!file.exists()) file.mkdir();
        File[] list = sourceDir.listFiles();
        for (int i = 0; i < list.length; i++) {
            if (list[i].isDirectory()) continue;
            File temp = new File(file, list[i].getName());
            try {
                temp.createNewFile();
                GeneralSupport.copyFile(list[i], temp);
            } catch (IOException f) {
            }
        }
    }
