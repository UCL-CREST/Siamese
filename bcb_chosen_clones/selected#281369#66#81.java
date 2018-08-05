    private synchronized void ensureParsed() throws IOException, BadIMSCPException {
        if (cp != null) return;
        if (on_disk == null) {
            on_disk = createTemporaryFile();
            OutputStream to_disk = new FileOutputStream(on_disk);
            IOUtils.copy(in.getInputStream(), to_disk);
            to_disk.close();
        }
        try {
            ZipFilePackageParser parser = utils.getIMSCPParserFactory().createParser();
            parser.parse(on_disk);
            cp = parser.getPackage();
        } catch (BadParseException x) {
            throw new BadIMSCPException("Cannot parse content package", x);
        }
    }
