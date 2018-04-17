    private void prepareQueryResultData(ZipEntryRef zer, String nodeDir, String reportDir, Set<ZipEntryRef> statusZers) throws Exception {
        String jobDir = nodeDir + File.separator + "job_" + zer.getUri();
        if (!WorkDirectory.isWorkingDirectoryValid(jobDir)) {
            throw new Exception("Cannot acces to " + jobDir);
        }
        File f = new File(jobDir + File.separator + "result.xml");
        if (!f.exists() || !f.isFile() || !f.canRead()) {
            throw new Exception("Cannot acces to result file " + f.getAbsolutePath());
        }
        String fcopyName = reportDir + File.separator + zer.getName() + ".xml";
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fcopyName));
        IOUtils.copy(bis, bos);
        bis.close();
        bos.close();
        zer.setUri(fcopyName);
        f = new File(jobDir + File.separator + "status.xml");
        if (!f.exists() || !f.isFile() || !f.canRead()) {
            throw new Exception("Cannot acces to status file " + f.getAbsolutePath());
        }
        fcopyName = reportDir + File.separator + zer.getName() + "_status.xml";
        bis = new BufferedInputStream(new FileInputStream(f));
        bos = new BufferedOutputStream(new FileOutputStream(fcopyName));
        IOUtils.copy(bis, bos);
        bis.close();
        bos.close();
        statusZers.add(new ZipEntryRef(ZipEntryRef.SINGLE_FILE, zer.getName(), fcopyName, ZipEntryRef.WITH_REL));
    }
