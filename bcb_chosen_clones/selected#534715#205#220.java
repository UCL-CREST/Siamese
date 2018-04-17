    public static boolean copyDirectory(String dir_in, String dir_out) {
        File f_in = new File(dir_in);
        File f_out = new File(dir_out);
        if ((!f_out.isDirectory() && f_out.exists()) || !f_in.exists()) return false;
        if (!f_out.isDirectory()) f_out.mkdir();
        String[] lista_file = f_in.list();
        for (int i = 0; i < lista_file.length; i++) {
            File f = new File(f_in.getAbsolutePath() + File.separator + lista_file[i]);
            if (f.isDirectory()) {
                copyDirectory(f.getAbsolutePath(), f_out.getAbsolutePath() + File.separator + f.getName());
            } else {
                copyFile(f_in.getAbsolutePath() + File.separator + f.getName(), f_out.getAbsolutePath() + File.separator + f.getName());
            }
        }
        return true;
    }
