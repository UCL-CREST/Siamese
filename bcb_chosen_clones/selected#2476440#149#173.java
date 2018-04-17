    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        if (version == 0) {
            version = 3;
        }
        out.writeLong(version);
        switch(version) {
            case 2:
                out.writeObject(mask);
                break;
            case 3:
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                ZipOutputStream zipStream = new ZipOutputStream(byteStream);
                zipStream.putNextEntry(new ZipEntry("mask"));
                ObjectOutputStream objStream = new ObjectOutputStream(zipStream);
                objStream.writeObject(mask);
                objStream.close();
                zipStream.close();
                byte[] compressed = byteStream.toByteArray();
                out.writeObject(compressed);
                byteStream.close();
                break;
            default:
                throw new RuntimeException("version not supported: " + version);
        }
    }
