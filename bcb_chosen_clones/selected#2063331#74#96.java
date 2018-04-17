    protected List<Datastream> getDatastreams(final DepositCollection pDeposit) throws IOException, SWORDException {
        List<Datastream> tDatastreams = super.getDatastreams(pDeposit);
        FileInputStream tInput = null;
        String tFileName = ((LocalDatastream) tDatastreams.get(0)).getPath();
        String tTempFileName = this.getTempDir() + "uploaded-file.tmp";
        IOUtils.copy(tInput = new FileInputStream(tFileName), new FileOutputStream(tTempFileName + ".thum"));
        tInput.close();
        Datastream tThum = new LocalDatastream("THUMBRES_IMG", this.getContentType(), tTempFileName + ".thum");
        tDatastreams.add(tThum);
        IOUtils.copy(tInput = new FileInputStream(tFileName), new FileOutputStream(tTempFileName + ".mid"));
        tInput.close();
        Datastream tMid = new LocalDatastream("MEDRES_IMG", this.getContentType(), tTempFileName + ".mid");
        tDatastreams.add(tMid);
        IOUtils.copy(tInput = new FileInputStream(tFileName), new FileOutputStream(tTempFileName + ".high"));
        tInput.close();
        Datastream tLarge = new LocalDatastream("HIGHRES_IMG", this.getContentType(), tTempFileName + ".high");
        tDatastreams.add(tLarge);
        IOUtils.copy(tInput = new FileInputStream(tFileName), new FileOutputStream(tTempFileName + ".vhigh"));
        tInput.close();
        Datastream tVLarge = new LocalDatastream("VERYHIGHRES_IMG", this.getContentType(), tTempFileName + ".vhigh");
        tDatastreams.add(tVLarge);
        return tDatastreams;
    }
