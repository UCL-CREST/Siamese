    public OCFContainerWriter(OutputStream out, String mime) throws IOException {
        zip = new ZipOutputStream(out);
        try {
            byte[] bytes = mime.getBytes("UTF-8");
            ZipEntry mimetype = new ZipEntry("mimetype");
            mimetype.setMethod(ZipOutputStream.STORED);
            mimetype.setSize(bytes.length);
            mimetype.setCompressedSize(bytes.length);
            CRC32 crc = new CRC32();
            crc.update(bytes);
            mimetype.setCrc(crc.getValue());
            zip.putNextEntry(mimetype);
            zip.write(bytes);
            zip.closeEntry();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
