    public ByteArray zip(Value request) throws FaultException {
        ByteArrayOutputStream bbstream = new ByteArrayOutputStream();
        try {
            ZipOutputStream zipStream = new ZipOutputStream(bbstream);
            ZipEntry zipEntry;
            byte[] bb;
            for (Entry<String, ValueVector> entry : request.children().entrySet()) {
                zipEntry = new ZipEntry(entry.getKey());
                zipStream.putNextEntry(zipEntry);
                bb = entry.getValue().first().byteArrayValue().getBytes();
                zipStream.write(bb, 0, bb.length);
                zipStream.closeEntry();
            }
            zipStream.close();
        } catch (IOException e) {
            throw new FaultException(e);
        }
        return new ByteArray(bbstream.toByteArray());
    }
