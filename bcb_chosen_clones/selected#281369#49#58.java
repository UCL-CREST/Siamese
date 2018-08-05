    public void serialize(OutputStream out) throws IOException, BadIMSCPException {
        ensureParsed();
        ZipFilePackageParser parser = utils.getIMSCPParserFactory().createParser();
        parser.setContentPackage(cp);
        if (on_disk != null) on_disk.delete();
        on_disk = createTemporaryFile();
        parser.serialize(on_disk);
        InputStream in = new FileInputStream(on_disk);
        IOUtils.copy(in, out);
    }
