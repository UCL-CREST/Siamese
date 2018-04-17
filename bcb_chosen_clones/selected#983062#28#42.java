    public static void ZipString(StringBuffer[] data, String[] FileName, OutputStream os) throws IOException {
        ZipOutputStream tempZStream = null;
        ZipEntry tempEntry = null;
        tempZStream = new ZipOutputStream(os);
        for (int i = 0; i < data.length; i++) {
            tempEntry = new ZipEntry(FileName[i]);
            tempEntry.setMethod(ZipEntry.DEFLATED);
            tempEntry.setSize((long) data[i].toString().getBytes().length);
            tempZStream.putNextEntry(tempEntry);
            tempZStream.write(data[i].toString().getBytes(), 0, data[i].toString().getBytes().length);
        }
        tempZStream.flush();
        os.flush();
        tempZStream.close();
    }
