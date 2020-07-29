    public FtpConfig(File cfgFile) throws Exception {
        super(cfgFile);
        mCfgFile = cfgFile;
        mServerAddress = getInetAddress(PREFIX + "server.host", null);
        mSelfAddress = getInetAddress(PREFIX + "self.host", null);
        miServerPort = getInteger(PREFIX + "port", 21);
        miMaxLogin = getInteger(PREFIX + "login", 20);
        mbAnonAllowed = getBoolean(PREFIX + "anonymous", true);
        miAnonLogin = getInteger(PREFIX + "anonymous.login", 10);
        miPollInterval = getInteger(PREFIX + "poll.interval", 60);
        mlLogMaxSize = getLong(PREFIX + "log.size", 1024) * 1024;
        mbLogFlush = getBoolean(PREFIX + "log.flush", false);
        miLogLevel = getInteger(PREFIX + "log.level", 1);
        mDefaultRoot = getFile(PREFIX + "root.dir", new File("/"));
        miDefaultIdle = getInteger(PREFIX + "idle.time", 300);
        mDataDir = getFile(PREFIX + "data", new File("./data"));
        mbAllowIp = getBoolean(PREFIX + "ip.allow", false);
        mbCreateHome = getBoolean(PREFIX + "home.create", false);
        String s = getString(PREFIX + "data.port.pool", "0");
        StringTokenizer st = new StringTokenizer(s, ", \t\n\r\f");
        miDataPort = new int[st.countTokens()][2];
        for (int i = 0; i < miDataPort.length; i++) {
            miDataPort[i][0] = Integer.parseInt(st.nextToken());
            miDataPort[i][1] = 0;
        }
        if (mSelfAddress == null) {
            mSelfAddress = InetAddress.getLocalHost();
        }
        if (mServerAddress == null) {
            mServerAddress = mSelfAddress;
        }
        File logDir = new File(mDataDir, "log");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        mFtpLog = new LogFile(new File(logDir, LOG_FILE));
        mFtpLog.setMaxSize(mlLogMaxSize);
        mFtpLog.setAutoFlush(mbLogFlush);
        mFtpLog.setLogLevel(miLogLevel);
        File ipDat = new File(mDataDir, IP_PROP);
        if (!ipDat.exists()) {
            ipDat.createNewFile();
        }
        mIpRestrictor = new IpRestrictor(ipDat, mbAllowIp);
        Class managerClass = getClass(PREFIX + "user.manager", PropertiesUserManager.class);
        Constructor cons = managerClass.getConstructor(new Class[] { getClass() });
        mUserManager = (UserManager) cons.newInstance(new Object[] { this });
        mStatistics = new FtpStatistics(this);
        mConService = new ConnectionService(this);
        mStatus = new FtpStatus();
        mQueue = new AsyncMessageQueue();
        mQueue.setMaxSize(2048);
        mFtpLog.info("Configuration loaded " + mCfgFile.getAbsolutePath());
    }
