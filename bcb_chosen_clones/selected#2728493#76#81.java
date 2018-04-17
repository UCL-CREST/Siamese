    public static void emptyDirectory(File f) throws IOException {
        if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "empty directory " + f.getAbsolutePath());
        if (f.isDirectory()) {
            for (File c : f.listFiles()) delete(c);
        }
    }
