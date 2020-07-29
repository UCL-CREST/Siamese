    public static Observacion load() {
        Observacion obs = new Observacion(new Date());
        FTPClient f = new FTPClient();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final String datestamp = sdf.format(new Date());
        String pathname = String.format(PATHNAME_PATTERN, datestamp);
        try {
            InetAddress server = InetAddress.getByName(HOST);
            f.connect(server);
            f.login(USERNAME, PASSWORD);
            FTPFile[] files = f.listFiles(pathname, new FTPFileFilter() {

                @Override
                public boolean accept(FTPFile file) {
                    return file.getName().startsWith(datestamp);
                }
            });
            FTPFile file = files[files.length - 1];
            f.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
            boolean download = false;
            String remote = pathname + "/" + file.getName();
            if (download) {
                File out = new File("/home/randres/Desktop/" + file.getName());
                FileOutputStream fout = new FileOutputStream(out);
                fout.flush();
                fout.close();
            } else {
                GZIPInputStream gzipin = new GZIPInputStream(f.retrieveFileStream(remote));
                LineNumberReader lreader = new LineNumberReader(new InputStreamReader(gzipin, "Cp1250"));
                String line = null;
                while ((line = lreader.readLine()) != null) {
                    obs.addEstacion(AemetRetriever.processLine(line));
                }
                lreader.close();
            }
            f.disconnect();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Cannot retrieve data from FTP", e);
        }
        return obs;
    }
