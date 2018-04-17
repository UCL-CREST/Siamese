    public static void emptyDirectory(File f, String except) throws IOException {
        if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "empty directory " + f.getAbsolutePath());
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                if (except != null && !c.getName().equals(except)) {
                    delete(c);
                }
            }
        }
    }
