    protected static void saveSession(DeviceSession session, File file) throws IOException {
        if (!(session instanceof Impl_DeviceSession)) session = new Impl_DeviceSession(session.getDevice());
        ZipOutputStream zos = null;
        ObjectOutputStream oos = null;
        zos = new ZipOutputStream(new FileOutputStream(file));
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.setLevel(Deflater.BEST_SPEED);
        zos.putNextEntry(new ZipEntry(SESSION_CONTENT_ENTRY));
        oos = new ObjectOutputStream(zos);
        oos.writeObject(session.getRomAndFlash());
        oos.writeObject(session);
        oos.close();
    }
