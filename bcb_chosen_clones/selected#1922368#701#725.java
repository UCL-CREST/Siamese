    public static void copyFileOrDirectory(File from_file_or_dir, File to_parent_dir) throws IOException {
        if (!from_file_or_dir.exists()) {
            throw (new IOException("File '" + from_file_or_dir.toString() + "' doesn't exist"));
        }
        if (!to_parent_dir.exists()) {
            throw (new IOException("File '" + to_parent_dir.toString() + "' doesn't exist"));
        }
        if (!to_parent_dir.isDirectory()) {
            throw (new IOException("File '" + to_parent_dir.toString() + "' is not a directory"));
        }
        if (from_file_or_dir.isDirectory()) {
            File[] files = from_file_or_dir.listFiles();
            File new_parent = new File(to_parent_dir, from_file_or_dir.getName());
            FileUtil.mkdirs(new_parent);
            for (int i = 0; i < files.length; i++) {
                File from_file = files[i];
                copyFileOrDirectory(from_file, new_parent);
            }
        } else {
            File target = new File(to_parent_dir, from_file_or_dir.getName());
            if (!copyFile(from_file_or_dir, target)) {
                throw (new IOException("File copy from " + from_file_or_dir + " to " + target + " failed"));
            }
        }
    }
