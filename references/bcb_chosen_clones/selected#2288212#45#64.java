    public static void copyDirectory(File source, File to) throws IOException {
        if (source.isDirectory()) {
            if (!to.exists()) {
                to.mkdir();
            }
            String[] children = source.list();
            for (int i = 0; i < children.length; i++) {
                File newSource = new File(source, children[i]);
                File newTo = new File(to, children[i]);
                copyDirectory(newSource, newTo);
            }
        } else {
            try {
                copyFile(source, to);
            } catch (Exception e) {
                e.printStackTrace();
                throw new IOException("It can't copy the file: " + source.getName());
            }
        }
    }
