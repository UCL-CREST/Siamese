    private byte[] zip() {
        try {
            baos.reset();
            ZipOutputStream zos = new ZipOutputStream(baos);
            for (int ii = 0; ii < sinkMap.NumberOfChannels(); ++ii) {
                byte[] data = sinkMap.GetData(ii);
                ZipEntry ze = new ZipEntry(sinkMap.GetName(ii));
                zos.putNextEntry(ze);
                zos.write(data);
            }
            zos.finish();
            return baos.toByteArray();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return null;
    }
