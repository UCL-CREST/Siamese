    private void delete(File f, boolean force, boolean recurse) {
        if (f.exists()) {
            if (f.isDirectory()) {
                if (!recurse) {
                    printErr("Is a directory: " + f.getPath());
                    return;
                }
                File files[] = f.listFiles();
                for (File subf : files) {
                    delete(subf, force, recurse);
                }
            }
            if (!force && !f.canWrite()) {
                printErr("File is not writable: " + f.getPath());
                return;
            }
            if (!f.delete()) {
                printErr("Error deleting file: " + f.getPath());
            }
        } else if (!force) printErr("File does not exist: " + f.getPath());
    }
