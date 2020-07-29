    public static void recursivelyDeleteDirectory(File dir) throws IOException {
        if ((dir == null) || !dir.isDirectory()) throw new IllegalArgumentException(dir + " not a directory");
        final File[] files = dir.listFiles();
        final int size = files.length;
        for (int i = 0; i < size; i++) {
            if (files[i].isDirectory()) {
                recursivelyDeleteDirectory(files[i]);
            } else {
                if (files[i].delete()) System.out.println(files[i].getPath() + " file was deleted"); else System.out.println(files[i].getPath() + " file cant be deleted");
            }
        }
        dir.delete();
    }
