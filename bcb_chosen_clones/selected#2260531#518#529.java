    public static boolean copyDirectory(File from, File to) throws IOException {
        if ((!(from.isDirectory()) && (to.isDirectory())) || (from == to)) {
            return false;
        }
        String[] children = from.list();
        for (int i = 0; i < children.length; i++) {
            File fromfile = new File(from, children[i]);
            File tofile = new File(to, children[i]);
            copyFile(fromfile, tofile);
        }
        return true;
    }
