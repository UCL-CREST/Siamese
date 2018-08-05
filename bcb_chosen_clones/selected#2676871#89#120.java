    public static void copyFile(File from, File to) throws IOException {
        if (from.isDirectory()) {
            if (!to.exists()) {
                to.mkdir();
            }
            File[] children = from.listFiles();
            for (int i = 0; i < children.length; i++) {
                if (children[i].getName().equals(".") || children[i].getName().equals("..")) {
                    continue;
                }
                if (children[i].isDirectory()) {
                    File f = new File(to, children[i].getName());
                    copyFile(children[i], f);
                } else {
                    copyFile(children[i], to);
                }
            }
        } else if (from.isFile() && (to.isDirectory() || to.isFile())) {
            if (to.isDirectory()) {
                to = new File(to, from.getName());
            }
            FileInputStream in = new FileInputStream(from);
            FileOutputStream out = new FileOutputStream(to);
            byte[] buf = new byte[32678];
            int read;
            while ((read = in.read(buf)) > -1) {
                out.write(buf, 0, read);
            }
            closeStream(in);
            closeStream(out);
        }
    }
