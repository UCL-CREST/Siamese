    @Override
    public void perform(IsolationAdmin admin) throws Exception {
        switch(type) {
            case INSTALL:
                {
                    IsolatedFramework fw = admin.getIsolatedFramework(URI.create(this.getTarget()));
                    byte[] data = null;
                    if (location.startsWith("file:")) {
                        File f = new File(URI.create(location));
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Streams.drain(new FileInputStream(f), baos).get();
                        data = baos.toByteArray();
                    }
                    fw.installBundle(location, data);
                    break;
                }
            case UNINSTALL:
                {
                    Bundle b = admin.getBundle(location);
                    if (b == null) {
                        LOGGER.warn("bundle with location {} not found", location);
                        return;
                    }
                    b.uninstall();
                    break;
                }
        }
    }
