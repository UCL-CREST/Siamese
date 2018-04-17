    @Override
    public boolean saveImpl(Document content, OutputStream out) {
        ZipOutputStream zos = null;
        if (out instanceof ZipOutputStream) zos = (ZipOutputStream) out; else zos = new ZipOutputStream(out);
        ZipEntry partEntry = new ZipEntry(CONTENT_TYPES_PART_NAME);
        try {
            zos.putNextEntry(partEntry);
            ByteArrayOutputStream outTemp = new ByteArrayOutputStream();
            StreamHelper.saveXmlInStream(content, out);
            InputStream ins = new ByteArrayInputStream(outTemp.toByteArray());
            byte[] buff = new byte[ZipHelper.READ_WRITE_FILE_BUFFER_SIZE];
            while (ins.available() > 0) {
                int resultRead = ins.read(buff);
                if (resultRead == -1) {
                    break;
                } else {
                    zos.write(buff, 0, resultRead);
                }
            }
            zos.closeEntry();
        } catch (IOException ioe) {
            logger.error("Cannot write: " + CONTENT_TYPES_PART_NAME + " in Zip !", ioe);
            return false;
        }
        return true;
    }
