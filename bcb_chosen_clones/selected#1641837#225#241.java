    public static void recurseDirectoryCopy(File _src, File _dest) {
        File[] srcList = _src.listFiles();
        for (int i = 0; i < srcList.length; i++) {
            String name = srcList[i].getName();
            File dest = new File(_dest, name);
            if (srcList[i].isDirectory()) {
                dest.mkdirs();
                recurseDirectoryCopy(srcList[i], dest);
            } else {
                try {
                    writeBytesToFile(getFileAsBytes(new File(_src, name)), dest);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
