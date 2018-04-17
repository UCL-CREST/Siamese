    public static void copyDir(File from, File to) throws IOException {
        assert (from.isDirectory());
        assert (to.isDirectory());
        List<File> files = getAllFiles(from);
        for (File fileFrom : files) {
            String toPath = fileFrom.getAbsolutePath().replace(from.getAbsolutePath(), to.getAbsolutePath());
            File fileTo = new File(toPath);
            fileTo.getParentFile().mkdirs();
            copy(fileFrom, fileTo);
        }
    }
