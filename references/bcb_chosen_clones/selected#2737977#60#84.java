    public List<Datastream> getFiles(final String pFile) throws IOException {
        List<Datastream> tDatastreams = new ArrayList<Datastream>();
        new File(this.getTmpExtractDirName()).mkdir();
        ZipFile tZipFile = new ZipFile(pFile);
        Enumeration tEntries = tZipFile.entries();
        ZipEntry tEntry = null;
        File tFile = null;
        String tFileLocation = "";
        LocalDatastream tLocalDs = null;
        while (tEntries.hasMoreElements()) {
            tEntry = (ZipEntry) tEntries.nextElement();
            if (tEntry.isDirectory()) {
                continue;
            }
            tFileLocation = this.getTmpExtractDirName() + System.getProperty("file.separator") + tEntry.getName();
            tFile = new File(tFileLocation);
            LOG.debug("Saving " + tEntry.getName() + " to " + tFile.getPath());
            tFile.getParentFile().mkdirs();
            IOUtils.copy(tZipFile.getInputStream(tEntry), new FileOutputStream(tFile));
            tLocalDs = new LocalDatastream(tFile.getName().split("\\.")[0], FindMimeType.getMimeType(tFile.getName().split("\\.")[1]), tFileLocation);
            tLocalDs.setLabel(tEntry.getName());
            tDatastreams.add(tLocalDs);
        }
        return tDatastreams;
    }
