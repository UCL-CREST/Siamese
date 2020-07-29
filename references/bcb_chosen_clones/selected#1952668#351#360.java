    public static boolean deletedir(File f) {
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) deletedir(files[i]); else deletefile(files[i]);
            }
        }
        f.delete();
        return true;
    }
