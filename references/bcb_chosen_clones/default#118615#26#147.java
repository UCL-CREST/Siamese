    public ServerLauncher() {
        super();
        startTime = new Date();
        lastSave = startTime;
        try {
            FileInputStream fis = new FileInputStream("ServerLauncher.properties");
            Properties p = new Properties();
            p.load(fis);
            String value = p.getProperty("allowhelp");
            if (value != null && value.equalsIgnoreCase("false")) {
                allowHelp = false;
            } else {
                allowHelp = true;
            }
            value = p.getProperty("tpfortrusted");
            if (value != null && value.equalsIgnoreCase("false")) {
                trustedTP = false;
            } else {
                trustedTP = true;
            }
            value = p.getProperty("fun");
            if (value != null && value.equalsIgnoreCase("true")) {
                isFun = true;
                System.out.println("Fun mode is enabled.");
            } else {
                isFun = false;
                System.out.println("Fun mode is disabled.");
            }
            value = p.getProperty("itemwhitelist");
            if (value != null && value.equalsIgnoreCase("true")) {
                isItemWhiteList = true;
            } else {
                isItemWhiteList = false;
            }
            value = p.getProperty("playerwhitelist");
            if (value != null && value.equalsIgnoreCase("true")) {
                isPlayerWhiteList = true;
            } else {
                isPlayerWhiteList = false;
            }
            value = p.getProperty("playercap");
            if (value != null) {
                try {
                    myPlayerCap = Integer.parseInt(value);
                    System.out.println("Setting player cap to " + myPlayerCap + ".");
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: Invalid number for player cap.");
                    myPlayerCap = -1;
                }
            } else {
                myPlayerCap = -1;
            }
            value = p.getProperty("ram");
            if (value != null) {
                try {
                    myRAMUse = Integer.parseInt(value);
                    System.out.println("Setting ram use to " + value + "mb.");
                } catch (NumberFormatException e) {
                    System.err.println("ERROR: Invalid amount of ram.");
                    myRAMUse = 1024;
                }
            } else {
                myRAMUse = 1024;
            }
            value = p.getProperty("autosave");
            if (value != null && value.equalsIgnoreCase("false")) {
                autoSave = false;
            } else {
                autoSave = true;
            }
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                System.err.println("ERROR: Cannot open ServerLauncher.properties");
                Properties p = new Properties();
                p.setProperty("fun", "false");
                p.setProperty("itemwhitelist", "true");
                p.setProperty("playerwhitelist", "false");
                p.setProperty("playercap", "-1");
                p.setProperty("ram", "1024");
                p.setProperty("autosave", "true");
                p.setProperty("allowHelp", "true");
                p.setProperty("tpfortrusted", "true");
                try {
                    p.store(new FileOutputStream("ServerLauncher.properties"), "Properties for ServerLauncher");
                } catch (IOException f) {
                }
            } else {
                System.err.println("ERROR: ServerLauncher.properties is of an invalid format.");
            }
            Properties p = new Properties();
            isFun = false;
            isPlayerWhiteList = true;
            isItemWhiteList = false;
            myPlayerCap = -1;
            myRAMUse = 1024;
            autoSave = true;
            allowHelp = true;
            trustedTP = true;
        }
        try {
            myProcess = Runtime.getRuntime().exec("java -Xms" + myRAMUse + "m -Xmx" + myRAMUse + "m -jar minecraft_server.jar nogui");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not run the server.");
            System.exit(1);
        }
        myInReader = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));
        myErrReader = new BufferedReader(new InputStreamReader(myProcess.getErrorStream()));
        myWriter = new PrintWriter(new OutputStreamWriter(myProcess.getOutputStream()), true);
        loadLists();
        keep_going = true;
        myPlayerCount = 0;
        connectedPlayers = "";
        ban_command = "ban";
        if (isPlayerWhiteList) {
            ban_command = "allow";
        }
        list_command = "blacklist";
        if (isItemWhiteList) {
            list_command = "whitelist";
        }
    }
