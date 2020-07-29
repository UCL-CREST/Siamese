    public File getPathtoDb() {
        String tmpLine = null;
        String usrFolder = null;
        String getOs = null;
        String localPath = null;
        String[] configLine = new String[2];
        String dbfile = null;
        try {
            FileReader input = null;
            BufferedReader bufInput = null;
            getOs = System.getProperty("os.name");
            usrFolder = System.getProperty("user.home");
            for (int i = 0; i < constants.osVer.length; i++) {
                while (getOs.equals(constants.osVer[i])) {
                    localPath = constants.folderPath[i];
                    input = new FileReader(usrFolder + constants.profileCfg[i]);
                    bufInput = new BufferedReader(input);
                    while ((tmpLine = bufInput.readLine()) != null) {
                        if (tmpLine.startsWith("Path=")) {
                            configLine = tmpLine.split("Path=");
                        }
                    }
                    dbfile = usrFolder + localPath + configLine[1] + "/" + constants.db_file;
                    break;
                }
            }
            if (dbfile == null) {
                openFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = openFile.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    dbfile = openFile.getSelectedFile().toString();
                } else {
                    dbfile = null;
                    msg.shwMsg("You must choose DB file in order to proceed", "Error:", 0, 1);
                    System.exit(0);
                }
            } else {
                dbfile = usrFolder + localPath + configLine[1] + "/" + constants.db_file;
            }
            System.out.println("Using DB:" + dbfile);
            return (new File(dbfile));
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return null;
    }
