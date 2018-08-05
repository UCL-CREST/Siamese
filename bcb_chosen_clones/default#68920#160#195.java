    protected GatherBot(IniEditor settings2, Rcon rcon2) {
        live = false;
        topicChanged = true;
        ready = false;
        this.rcon = rcon2;
        this.settings = settings2;
        setName(settings.get("irc", "nick"));
        setVerbose(true);
        smartConnect(settings.get("irc", "ip"), Integer.parseInt(settings.get("irc", "port")));
        chan = settings.get("irc", "channel");
        unregistererror = settings.get("register", "unregistererror");
        joinChannel(chan);
        joinChannel("#Crit");
        sendMessage("Q@CServe.quakenet.org", "AUTH " + settings.get("irc", "qaccount") + " " + settings.get("irc", "qpassword"));
        setMode(getNick(), "+x");
        maxplayers = 12;
        players = new Players();
        maps = new ArrayList<Map>();
        if (settings.get("sql", "usemysql").equalsIgnoreCase("true")) {
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                sql = DriverManager.getConnection("jdbc:mysql://" + settings.get("sql", "ip") + ":" + settings.get("sql", "port") + "/" + settings.get("sql", "database"), settings.get("sql", "user"), settings.get("sql", "password")).createStatement();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Class.forName("org.sqlite.JDBC").newInstance();
                sql = DriverManager.getConnection("jdbc:sqlite:database.sqlite").createStatement();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        live = false;
        reg = true;
    }
