    public static void main(String[] args) throws Exception {
        PatternLayout pl = new PatternLayout("%d{ISO8601} %-5p %c: %m\n");
        ConsoleAppender ca = new ConsoleAppender(pl);
        Logger.getRoot().addAppender(ca);
        Logger.getRoot().setLevel(Level.INFO);
        Options options = new Options();
        options.addOption("p", "put", false, "put a file in the DHT overlay");
        options.addOption("g", "get", false, "get a file from the DHT");
        options.addOption("r", "remove", false, "remove a file from the DHT");
        options.addOption("u", "update", false, "updates the lease");
        options.addOption("j", "join", false, "join the DHT overlay");
        options.addOption("c", "config", true, "the configuration file");
        options.addOption("k", "key", true, "the key to read a file from");
        options.addOption("f", "file", true, "the file to read or write");
        options.addOption("a", "app", true, "the application ID");
        options.addOption("s", "secret", true, "the secret used to hide data");
        options.addOption("t", "ttl", true, "how long in seconds data should persist");
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);
        String configFile = null;
        String mode = null;
        String secretStr = null;
        int ttl = 9999;
        String keyStr = null;
        String file = null;
        int appId = 0;
        if (cmd.hasOption("j")) {
            mode = "join";
        }
        if (cmd.hasOption("p")) {
            mode = "put";
        }
        if (cmd.hasOption("g")) {
            mode = "get";
        }
        if (cmd.hasOption("r")) {
            mode = "remove";
        }
        if (cmd.hasOption("u")) {
            mode = "update";
        }
        if (cmd.hasOption("c")) {
            configFile = cmd.getOptionValue("c");
        }
        if (cmd.hasOption("k")) {
            keyStr = cmd.getOptionValue("k");
        }
        if (cmd.hasOption("f")) {
            file = cmd.getOptionValue("f");
        }
        if (cmd.hasOption("s")) {
            secretStr = cmd.getOptionValue("s");
        }
        if (cmd.hasOption("t")) {
            ttl = Integer.parseInt(cmd.getOptionValue("t"));
        }
        if (cmd.hasOption("a")) {
            appId = Integer.parseInt(cmd.getOptionValue("a"));
        }
        if (mode == null) {
            System.err.println("ERROR: --put or --get or --remove or --join or --update is required");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("DHT", options);
            System.exit(1);
        }
        if (configFile == null) {
            System.err.println("ERROR: --config is required");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("DHT", options);
            System.exit(1);
        }
        Properties conf = new Properties();
        conf.load(new FileInputStream(configFile));
        DHT dht = new DHT(conf);
        if (mode.equals("join")) {
            dht.join();
        } else if (mode.equals("put")) {
            if (file == null) {
                System.err.println("ERROR: --file is required");
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("DHT", options);
                System.exit(1);
            }
            if (keyStr == null) {
                System.err.println("ERROR: --key is required");
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("DHT", options);
                System.exit(1);
            }
            if (secretStr == null) {
                System.err.println("ERROR: --secret is required");
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("DHT", options);
                System.exit(1);
            }
            logger.info("putting file " + file);
            FileInputStream in = new FileInputStream(file);
            byte[] tmp = new byte[1000000];
            int num = in.read(tmp);
            byte[] value = new byte[num];
            System.arraycopy(tmp, 0, value, 0, num);
            in.close();
            if (dht.put((short) appId, keyStr.getBytes(), value, ttl, secretStr.getBytes()) < 0) {
                logger.info("There was an error while putting a key-value.");
                System.exit(0);
            }
            System.out.println("Ok!");
        } else if (mode.equals("get")) {
            if (file == null) {
                System.err.println("ERROR: --file is required");
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("DHT", options);
                System.exit(1);
            }
            if (keyStr == null) {
                System.err.println("ERROR: --key is required");
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("DHT", options);
                System.exit(1);
            }
            logger.info("getting file " + file);
            ArrayList<byte[]> values = new ArrayList<byte[]>();
            if (dht.get((short) appId, keyStr.getBytes(), Integer.MAX_VALUE, values) < 0) {
                logger.info("There was an error while getting a value.");
                System.exit(0);
            }
            if (values.size() == 0 || values == null) {
                System.out.println("No values returned.");
                System.exit(0);
            }
            FileOutputStream out = new FileOutputStream(file);
            System.out.println("Found " + values.size() + " values -- saving the first one only.");
            out.write(values.get(0));
            out.close();
            System.out.println("Ok!");
        } else if (mode.equals("remove")) {
            if (keyStr == null) {
                System.err.println("ERROR: --key is required");
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("DHT", options);
                System.exit(1);
            }
            if (secretStr == null) {
                System.err.println("ERROR: --secret is required");
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("DHT", options);
                System.exit(1);
            }
            logger.info("removing <key,value> for key=" + keyStr);
            if (dht.remove((short) appId, keyStr.getBytes(), secretStr.getBytes()) < 0) {
                logger.info("There was an error while removing a key.");
                System.exit(0);
            }
            System.out.println("Ok!");
        } else if (mode.equals("update")) {
            if (keyStr == null) {
                System.err.println("ERROR: --key is required");
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("DHT", options);
                System.exit(1);
            }
            logger.info("updating <key,value> for key=" + keyStr);
            if (dht.updateLease((short) appId, keyStr.getBytes(), ttl) < 0) {
                logger.info("There was an error while updating data lease.");
                System.exit(0);
            }
            System.out.println("Ok!");
        }
        DHT.getInstance().stop();
    }
