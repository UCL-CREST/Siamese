    private void writeInfos() throws IOException {
        zos.putNextEntry(new ZipEntry("info.bin"));
        outFile = new DataOutputStream(zos);
        outFile.writeInt(VERSION);
        outFile.writeInt(MINORVERSION);
        outFile.writeDouble(intervall_s);
        zos.closeEntry();
    }
