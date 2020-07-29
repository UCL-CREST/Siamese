    protected List<Datastream> getDatastreams(final DepositCollection pDeposit) throws IOException, SWORDException {
        List<Datastream> tDatastreams = new ArrayList<Datastream>();
        LOG.debug("copying file");
        String tZipTempFileName = super.getTempDir() + "uploaded-file.tmp";
        IOUtils.copy(pDeposit.getFile(), new FileOutputStream(tZipTempFileName));
        Datastream tDatastream = new LocalDatastream(super.getGenericFileName(pDeposit), this.getContentType(), tZipTempFileName);
        tDatastreams.add(tDatastream);
        tDatastreams.addAll(_zipFile.getFiles(tZipTempFileName));
        return tDatastreams;
    }
