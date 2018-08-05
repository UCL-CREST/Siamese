    public int read(String name) {
        status = STATUS_OK;
        try {
            name = name.trim();
            if (name.indexOf("://") > 0) {
                URL url = new URL(name);
                in = new BufferedInputStream(url.openStream());
            } else {
                in = new BufferedInputStream(new FileInputStream(name));
            }
            status = read(in);
        } catch (IOException e) {
            status = STATUS_OPEN_ERROR;
        }
        return status;
    }
