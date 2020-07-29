    @Override
    public void export(final Library lib) throws PluginException {
        try {
            new Thread(new Runnable() {

                public void run() {
                    formatter.format(lib, writer);
                    writer.flush();
                    writer.close();
                }
            }).start();
            ftp.connect(host);
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                ftp.disconnect();
                throw new PluginException("Unable to connect to FTP");
            }
            ftp.login(user, pass);
            ftp.pasv();
            ftp.changeWorkingDirectory(dir);
            ftp.storeFile(file, inStream);
            ftp.logout();
        } catch (SocketException e) {
            throw new PluginException(e);
        } catch (IOException e) {
            throw new PluginException(e);
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                }
            }
        }
    }
