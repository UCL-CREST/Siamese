    private void addZipEntry(String _filename, InputStream _in, ZipOutputStream _out) throws IOException {
        byte[] _buf = new byte[1024];
        _out.putNextEntry(new ZipEntry(_filename));
        int _len;
        while ((_len = _in.read(_buf)) > 0) _out.write(_buf, 0, _len);
        _out.closeEntry();
        _in.close();
    }
