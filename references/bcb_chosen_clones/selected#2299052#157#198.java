    public static void copy(File from_file, File to_file) throws IOException {
        if (!from_file.exists()) abort("FileCopy: no such source file: " + from_file.getName());
        if (!from_file.isFile()) abort("FileCopy: can't copy directory: " + from_file.getName());
        if (!from_file.canRead()) abort("FileCopy: source file is unreadable: " + from_file.getName());
        if (to_file.isDirectory()) to_file = new File(to_file, from_file.getName());
        if (to_file.exists()) {
            if (!to_file.canWrite()) abort("FileCopy: destination file is unwriteable: " + to_file.getName());
        } else {
            String parent = to_file.getParent();
            if (parent == null) parent = System.getProperty("user.dir");
            File dir = new File(parent);
            if (!dir.exists()) abort("FileCopy: destination directory doesn't exist: " + parent);
            if (dir.isFile()) abort("FileCopy: destination is not a directory: " + parent);
            if (!dir.canWrite()) abort("FileCopy: destination directory is unwriteable: " + parent);
        }
        FileInputStream from = null;
        FileOutputStream to = null;
        try {
            from = new FileInputStream(from_file);
            to = new FileOutputStream(to_file);
            byte[] buffer = new byte[4096];
            int bytes_read;
            while ((bytes_read = from.read(buffer)) != -1) {
                to.write(buffer, 0, bytes_read);
            }
        } finally {
            if (from != null) {
                try {
                    from.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (to != null) {
                try {
                    to.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
