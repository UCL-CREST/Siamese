    private static void copyFilesRecursively(File src, File dest, FileCopyMonitor mon) throws FileNotFoundException, IOException {
        if (mon.isCopyAborted()) return;
        if (!src.exists()) throw new FileNotFoundException("File not found:" + src);
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdirs();
            }
            String list[] = src.list();
            java.util.Arrays.sort(list);
            for (int i = 0; i < list.length; i++) {
                copyFilesRecursively(new File(src, list[i]), new File(dest, list[i]), mon);
            }
        } else {
            copy(src, dest, mon, true);
        }
    }
