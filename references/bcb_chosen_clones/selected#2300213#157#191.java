    private JeeObserverServerContext(JeeObserverServerContextProperties properties) throws DatabaseException, ServerException {
        super();
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(("JE" + System.currentTimeMillis()).getBytes());
            final BigInteger hash = new BigInteger(1, md5.digest());
            this.sessionId = hash.toString(16).toUpperCase();
        } catch (final Exception e) {
            this.sessionId = "JE" + System.currentTimeMillis();
            JeeObserverServerContext.logger.log(Level.WARNING, "JeeObserver Server session ID MD5 error: {0}", this.sessionId);
            JeeObserverServerContext.logger.log(Level.FINEST, e.getMessage(), e);
        }
        try {
            @SuppressWarnings("unchecked") final Class<DatabaseHandler> databaseHandlerClass = (Class<DatabaseHandler>) Class.forName(properties.getDatabaseHandler());
            final Constructor<DatabaseHandler> handlerConstructor = databaseHandlerClass.getConstructor(new Class<?>[] { String.class, String.class, String.class, String.class, String.class, Integer.class });
            this.databaseHandler = handlerConstructor.newInstance(new Object[] { properties.getDatabaseDriver(), properties.getDatabaseUrl(), properties.getDatabaseUser(), properties.getDatabasePassword(), properties.getDatabaseSchema(), new Integer(properties.getDatabaseConnectionPoolSize()) });
        } catch (final Exception e) {
            throw new ServerException("Database handler loading exception.", e);
        }
        this.databaseHandlerTimer = new Timer(JeeObserverServerContext.DATABASE_HANDLER_TASK_NAME, true);
        this.server = new JeeObserverServer(properties.getServerPort());
        this.enabled = true;
        this.properties = properties;
        this.startTimestamp = new Date();
        try {
            this.ip = InetAddress.getLocalHost().getHostAddress();
        } catch (final UnknownHostException e) {
            JeeObserverServerContext.logger.log(Level.SEVERE, e.getMessage(), e);
        }
        this.operatingSystemName = System.getProperty("os.name");
        this.operatingSystemVersion = System.getProperty("os.version");
        this.operatingSystemArchitecture = System.getProperty("os.arch");
        this.javaVersion = System.getProperty("java.version");
        this.javaVendor = System.getProperty("java.vendor");
    }
