    static void reopen(MJIEnv env, int objref) throws IOException {
        int fd = env.getIntField(objref, "fd");
        long off = env.getLongField(objref, "off");
        if (content.get(fd) == null) {
            int mode = env.getIntField(objref, "mode");
            int fnRef = env.getReferenceField(objref, "fileName");
            String fname = env.getStringObject(fnRef);
            if (mode == FD_READ) {
                FileInputStream fis = new FileInputStream(fname);
                FileChannel fc = fis.getChannel();
                fc.position(off);
                content.set(fd, fis);
            } else if (mode == FD_WRITE) {
                FileOutputStream fos = new FileOutputStream(fname);
                FileChannel fc = fos.getChannel();
                fc.position(off);
                content.set(fd, fos);
            } else {
                env.throwException("java.io.IOException", "illegal mode: " + mode);
            }
        }
    }
