    public Chalk() throws IOException {
        p = new ChalkProperties(config_file);
        listeners = new CopyOnWriteArrayList<ChalkListener>();
        try {
            if (p.containsKey("IP")) ip = InetAddress.getByName(p.getProperty("IP"));
        } catch (Exception e) {
            log("Couldn't parse setting for PORT - " + p.getProperty("PORT") + ". Using default " + port);
        }
        try {
            if (p.containsKey("PORT")) port = Integer.parseInt(p.getProperty("PORT"));
        } catch (Exception e) {
            log("Couldn't parse setting for PORT - " + p.getProperty("PORT") + ". Using default " + port);
        }
        if (ip == null) ip = InetAddress.getByName(Constants.IP);
        ss = new ServerSocket();
        ss.bind(new InetSocketAddress(ip, port));
        if (p.containsKey("VALIDATOR") && p.getProperty("VALIDATOR") != null) {
            try {
                Class<?> c = Class.forName(p.getProperty("VALIDATOR"));
                if (Validator.class.isAssignableFrom(c)) {
                    Constructor<?> ctr = c.getConstructor(ChalkProperties.class);
                    v = (Validator) ctr.newInstance(p);
                }
            } catch (Throwable t) {
                log("Failed to create user specified validator");
                t.printStackTrace();
            }
        }
        if (v == null) {
            v = new DefaultValidator(p);
        }
    }
