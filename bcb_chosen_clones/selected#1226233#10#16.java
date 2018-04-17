    FileCacheInputStreamFountain(FileCacheInputStreamFountainFactory factory, InputStream in) throws IOException {
        file = factory.createFile();
        OutputStream out = new FileOutputStream(file);
        IOUtils.copy(in, out);
        in.close();
        out.close();
    }
