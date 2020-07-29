    public static void copyDirectory(File source, File target) throws IOException {
        if (source.isDirectory()) {
            if (source.getName().toLowerCase().equals(".svn") || source.getName().toLowerCase().equals("cvs")) {
                return;
            }
            if (!target.exists()) {
                System.out.println(source.getName() + " - target doesn't exist!");
                return;
            }
            String[] children = source.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(source, children[i]), new File(target, children[i]));
            }
        } else {
            if (target.exists()) {
                if (source.lastModified() <= target.lastModified()) {
                    System.out.println(source.getName() + " - " + source.lastModified() + " " + target.lastModified());
                    return;
                }
            } else {
                return;
            }
            FileInputStream in = new FileInputStream(source);
            FileOutputStream out = new FileOutputStream(target);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
