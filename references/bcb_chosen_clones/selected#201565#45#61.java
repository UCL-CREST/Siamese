    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        if (!(object instanceof ZIPCompressedMessage)) return;
        ZIPCompressedMessage zipMessage = (ZIPCompressedMessage) object;
        Message message = zipMessage.getMessage();
        ByteBuffer tempBuffer = ByteBuffer.allocate(512000);
        Serializer.writeClassAndObject(tempBuffer, message);
        ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
        ZipOutputStream zipOutput = new ZipOutputStream(byteArrayOutput);
        zipOutput.setLevel(zipMessage.getLevel());
        ZipEntry zipEntry = new ZipEntry("zip");
        zipOutput.putNextEntry(zipEntry);
        zipOutput.write(tempBuffer.array());
        zipOutput.flush();
        zipOutput.closeEntry();
        zipOutput.close();
        buffer.put(byteArrayOutput.toByteArray());
    }
