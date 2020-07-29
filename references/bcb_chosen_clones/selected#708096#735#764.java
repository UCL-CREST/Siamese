    protected static void createBackup() throws IOException, IllegalStateException, FTPIllegalReplyException, FTPException, FileNotFoundException, FTPDataTransferException, FTPAbortedException {
        String cmd = "mysqldump -u " + Constants.dbUser + " -p" + Constants.dbPassword + " " + Constants.dbName + " > " + Constants.tmpDir + "Backup.sql";
        FileWriter fstream = new FileWriter(Constants.tmpDir + Constants.tmpScript);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(cmd);
        out.close();
        Process process = Runtime.getRuntime().exec(Constants.tmpDir + Constants.tmpScript);
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        while (br.readLine() != null) {
            ;
        }
        String fileName = now4backup();
        cmd = "\"C:\\Archivos de programa\\WinRAR\\Rar.exe\" a -m5 -ed " + Constants.tmpDir + fileName + " " + Constants.tmpDir + "Backup.sql";
        process = Runtime.getRuntime().exec(cmd);
        is = process.getInputStream();
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
        while (br.readLine() != null) {
            ;
        }
        FTPClient client = new FTPClient();
        client.connect(Constants.ftpBackupAddr);
        client.login(Constants.ftpBackupUser, Constants.ftpBackupPassword);
        client.changeDirectory("/" + Shared.getConfig("storeName"));
        File f = new File(Constants.tmpDir + fileName);
        client.upload(f);
        client.disconnect(false);
    }
