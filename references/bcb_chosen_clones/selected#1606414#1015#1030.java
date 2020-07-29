    private void addConfigurationFile(ZipOutputStream zos, GridQueue activeQueue, String verifierFilename) throws IOException {
        String sConf = "host = $host\n" + "username = $user\n" + "password = $pwd\n" + "database = $db\n" + "gridqueue = $q\n";
        DatabaseConnector con = DatabaseConnector.getInstance();
        sConf = sConf.replace("$host", con.getHostname());
        sConf = sConf.replace("$user", con.getUsername());
        sConf = sConf.replace("$pwd", con.getPassword());
        sConf = sConf.replace("$db", con.getDatabase());
        sConf = sConf.replace("$q", String.valueOf(activeQueue.getId()));
        if (verifierFilename != null) {
            sConf += "verifier = " + verifierFilename + "\n";
        }
        ZipEntry entry = new ZipEntry("config");
        zos.putNextEntry(entry);
        zos.write(sConf.getBytes());
        zos.closeEntry();
    }
