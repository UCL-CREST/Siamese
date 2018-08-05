    public osid.shared.Id ingest(String fileName, String templateFileName, String fileType, File file, Properties properties) throws osid.repository.RepositoryException, java.net.SocketException, java.io.IOException, osid.shared.SharedException, javax.xml.rpc.ServiceException {
        long sTime = System.currentTimeMillis();
        if (DEBUG) System.out.println("INGESTING FILE TO FEDORA:fileName =" + fileName + "fileType =" + fileType + "t = 0");
        String host = FedoraUtils.getFedoraProperty(this, "admin.ftp.address");
        String url = FedoraUtils.getFedoraProperty(this, "admin.ftp.url");
        int port = Integer.parseInt(FedoraUtils.getFedoraProperty(this, "admin.ftp.port"));
        String userName = FedoraUtils.getFedoraProperty(this, "admin.ftp.username");
        String password = FedoraUtils.getFedoraProperty(this, "admin.ftp.password");
        String directory = FedoraUtils.getFedoraProperty(this, "admin.ftp.directory");
        FTPClient client = new FTPClient();
        client.connect(host, port);
        client.login(userName, password);
        client.changeWorkingDirectory(directory);
        client.setFileType(FTP.BINARY_FILE_TYPE);
        client.storeFile(fileName, new FileInputStream(file));
        client.logout();
        client.disconnect();
        if (DEBUG) System.out.println("INGESTING FILE TO FEDORA: Writting to FTP Server:" + (System.currentTimeMillis() - sTime));
        fileName = url + fileName;
        int BUFFER_SIZE = 10240;
        StringBuffer sb = new StringBuffer();
        String s = new String();
        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(new File(getResource(templateFileName).getFile().replaceAll("%20", " "))));
        byte[] buf = new byte[BUFFER_SIZE];
        int ch;
        int len;
        while ((len = fis.read(buf)) > 0) {
            s = s + new String(buf);
        }
        fis.close();
        if (DEBUG) System.out.println("INGESTING FILE TO FEDORA: Read Mets File:" + (System.currentTimeMillis() - sTime));
        String r = updateMetadata(s, fileName, file.getName(), fileType, properties);
        if (DEBUG) System.out.println("INGESTING FILE TO FEDORA: Resplaced Metadata:" + (System.currentTimeMillis() - sTime));
        File METSfile = File.createTempFile("vueMETSMap", ".xml");
        FileOutputStream fos = new FileOutputStream(METSfile);
        fos.write(r.getBytes());
        fos.close();
        AutoIngestor a = new AutoIngestor(address.getHost(), address.getPort(), FedoraUtils.getFedoraProperty(this, "admin.fedora.username"), FedoraUtils.getFedoraProperty(this, "admin.fedora.username"));
        String pid = a.ingestAndCommit(new FileInputStream(METSfile), "Test Ingest");
        if (DEBUG) System.out.println("INGESTING FILE TO FEDORA: Ingest complete:" + (System.currentTimeMillis() - sTime));
        System.out.println(" METSfile= " + METSfile.getPath() + " PID = " + pid);
        return new PID(pid);
    }
