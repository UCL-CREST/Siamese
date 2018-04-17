    private static final boolean rmdir(File d) {
        log.debug("TRACE: rmdir(" + d + ")");
        if (!d.exists()) return false;
        if (d.isFile()) {
            return d.delete();
        }
        File[] list = d.listFiles();
        if (list.length == 0) {
            return d.delete();
        }
        for (int i = 0; i < list.length; i++) if (list[i].isDirectory()) {
            return rmdir(list[i]);
        } else {
            list[i].delete();
        }
        return false;
    }
