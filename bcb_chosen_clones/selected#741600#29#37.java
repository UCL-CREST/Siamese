    PackageFileImpl(PackageDirectoryImpl dir, String name, InputStream data) throws IOException {
        this.dir = dir;
        this.name = name;
        this.updates = dir.getUpdates();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        IOUtils.copy(data, stream);
        updates.setNewData(getFullName(), stream.toByteArray());
        stream.close();
    }
