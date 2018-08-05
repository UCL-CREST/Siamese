    public static void main(String[] args) {
        FTPClient f = new FTPClient();
        String host = "ftpdatos.aemet.es";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final String datestamp = sdf.format(new Date());
        System.out.println(datestamp);
        String pathname = "datos_observacion/observaciones_diezminutales/" + datestamp + "_diezminutales/";
        try {
            InetAddress server = InetAddress.getByName(host);
            f.connect(server);
            String username = "anonymous";
            String password = "a@b.c";
            f.login(username, password);
            FTPFile[] files = f.listFiles(pathname, new FTPFileFilter() {

                @Override
                public boolean accept(FTPFile file) {
                    return file.getName().startsWith(datestamp);
                }
            });
            FTPFile file = files[files.length - 2];
            f.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
            boolean download = false;
            String remote = pathname + "/" + file.getName();
            if (download) {
                File out = new File("/home/randres/Desktop/" + file.getName());
                FileOutputStream fout = new FileOutputStream(out);
                System.out.println(f.retrieveFile(remote, fout));
                fout.flush();
                fout.close();
            } else {
                GZIPInputStream gzipin = new GZIPInputStream(f.retrieveFileStream(remote));
                LineNumberReader lreader = new LineNumberReader(new InputStreamReader(gzipin, "Cp1250"));
                String line = null;
                while ((line = lreader.readLine()) != null) {
                    Aeminuto.processLine(line);
                }
                lreader.close();
            }
            f.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
