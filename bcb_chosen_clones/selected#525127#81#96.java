    public Peer2SupernodeImpl() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(System.getProperty("pubweb.userdb")));
        for (Entry<Object, Object> entry : properties.entrySet()) {
            String v = (String) entry.getValue();
            users.put((String) entry.getKey(), new UserMetaData(v.substring(0, v.length() - 2), v.charAt(v.length() - 1) == 'x'));
        }
        try {
            @SuppressWarnings("unchecked") Class<Scheduler> c = (Class<Scheduler>) Class.forName(System.getProperty("pubweb.sched"));
            scheduler = (Scheduler) c.getConstructor(SchedulerListener.class).newInstance(this);
        } catch (Throwable t) {
            System.err.println("warning: could not load scheduler -- using default implementation:");
            t.printStackTrace();
            scheduler = new SimpleScheduler(this);
        }
    }
