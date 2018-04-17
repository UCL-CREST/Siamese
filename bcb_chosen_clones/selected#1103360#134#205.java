    public static void creme_connect(String email, String username, String password, String IDnumber, TRP[] trp_array, GTRN[] gtrn_array, FLUX[] flux_array, TRANS[] trans_array, LETSPEC[] letspec_array, PUP[] pup_array, HUP[] hup_array, DOSE[] dose_array) {
        int num_of_files = trp_array.length + gtrn_array.length + flux_array.length + trans_array.length + letspec_array.length + pup_array.length + hup_array.length + dose_array.length;
        int index = 0;
        String[] files_to_upload = new String[num_of_files];
        for (int a = 0; a < trp_array.length; a++) {
            files_to_upload[index] = trp_array[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < gtrn_array.length; a++) {
            files_to_upload[index] = gtrn_array[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < flux_array.length; a++) {
            files_to_upload[index] = flux_array[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < trans_array.length; a++) {
            files_to_upload[index] = trans_array[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < letspec_array.length; a++) {
            files_to_upload[index] = letspec_array[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < pup_array.length; a++) {
            files_to_upload[index] = pup_array[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < hup_array.length; a++) {
            files_to_upload[index] = hup_array[a].getThisFileName();
            index++;
        }
        for (int a = 0; a < dose_array.length; a++) {
            files_to_upload[index] = dose_array[a].getThisFileName();
            index++;
        }
        Logger log = Logger.getLogger(CreateAStudy.class);
        String host = "creme96.nrl.navy.mil";
        String user = "anonymous";
        String ftppass = email;
        Logger.setLevel(Level.ALL);
        FTPClient ftp = null;
        try {
            ftp = new FTPClient();
            ftp.setRemoteHost(host);
            FTPMessageCollector listener = new FTPMessageCollector();
            ftp.setMessageListener(listener);
            log.info("Connecting");
            ftp.connect();
            log.info("Logging in");
            ftp.login(user, ftppass);
            log.debug("Setting up passive, ASCII transfers");
            ftp.setConnectMode(FTPConnectMode.ACTIVE);
            ftp.setType(FTPTransferType.BINARY);
            log.info("Putting file");
            for (int u = 0; u < files_to_upload.length; u++) {
                ftp.put(files_to_upload[u], files_to_upload[u]);
            }
            log.info("Quitting client");
            ftp.quit();
            log.debug("Listener log:");
            log.info("Test complete");
        } catch (Exception e) {
            log.error("Demo failed", e);
            e.printStackTrace();
        }
        System.out.println("Finished FTPing User Request Files to common directory");
        Upload_Files.upload(files_to_upload, username, password, IDnumber);
        System.out.println("Finished transfering User Request Files to your CREME96 personal directory");
        RunRoutines.routines(files_to_upload, username, password, IDnumber);
        System.out.println("Finished running all of your uploaded routines");
    }
