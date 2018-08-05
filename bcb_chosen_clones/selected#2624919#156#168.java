    protected static void saveRom(E4Device device) throws IOException {
        File file = new File(device.remote.getDeviceLocalDir(), LAST_ROM_SESSION_FILENAME + "." + SESSION_EXT);
        Object[] rom = device.sampleDB.getRomSnapshot();
        ZipOutputStream zos = null;
        ObjectOutputStream oos = null;
        zos = new ZipOutputStream(new FileOutputStream(file));
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.setLevel(Deflater.BEST_SPEED);
        zos.putNextEntry(new ZipEntry(FLASH_SESSION_CONTENT_ENTRY));
        oos = new ObjectOutputStream(zos);
        oos.writeObject(rom);
        oos.close();
    }
