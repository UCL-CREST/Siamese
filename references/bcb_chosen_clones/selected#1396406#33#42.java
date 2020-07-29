    public void addEntry(String name, byte[] byteArray) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        _zos.putNextEntry(entry);
        BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(byteArray), _BUFFER);
        int count;
        while ((count = bis.read(_data, 0, _BUFFER)) != -1) {
            _zos.write(_data, 0, count);
        }
        bis.close();
    }
