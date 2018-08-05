    public ZipViewdataReader(ZipInputStream stream) throws IOException {
        ZipEntry ze;
        while ((ze = stream.getNextEntry()) != null) {
            File temp = File.createTempFile("spool.", ".synu");
            temp.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(temp);
            byte[] buffer = new byte[1024 * 1024];
            int length;
            while ((length = stream.read(buffer)) != -1) fos.write(buffer, 0, length);
            fos.close();
            String name = ze.getName();
            String[] parts = name.split("[\\\\/]");
            this._file_hash.put(parts[parts.length - 1], temp);
        }
        stream.close();
        for (String key : this._file_hash.keySet()) if (key.endsWith("Viewdata")) {
            File f = this._file_hash.get(key);
            FileReader fr = new FileReader(f);
            this._viewdata = new BufferedReader(fr);
            break;
        }
        if (this._viewdata == null) throw new FileNotFoundException("No Viewdata found in ZIP file.");
    }
