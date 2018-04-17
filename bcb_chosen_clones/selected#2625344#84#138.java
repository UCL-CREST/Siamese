    @Override
    public void end() {
        m_zipFormatter.end();
        IOUtils.closeQuietly(m_outputStream);
        final FTPClient ftp = new FTPClient();
        FileInputStream fis = null;
        try {
            if (m_url.getPort() == -1 || m_url.getPort() == 0 || m_url.getPort() == m_url.getDefaultPort()) {
                ftp.connect(m_url.getHost());
            } else {
                ftp.connect(m_url.getHost(), m_url.getPort());
            }
            if (m_url.getUserInfo() != null && m_url.getUserInfo().length() > 0) {
                final String[] userInfo = m_url.getUserInfo().split(":", 2);
                ftp.login(userInfo[0], userInfo[1]);
            } else {
                ftp.login("anonymous", "opennmsftp@");
            }
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                LogUtils.errorf(this, "FTP server refused connection.");
                return;
            }
            String path = m_url.getPath();
            if (path.endsWith("/")) {
                LogUtils.errorf(this, "Your FTP URL must specify a filename.");
                return;
            }
            File f = new File(path);
            path = f.getParent();
            if (!ftp.changeWorkingDirectory(path)) {
                LogUtils.infof(this, "unable to change working directory to %s", path);
                return;
            }
            LogUtils.infof(this, "uploading %s to %s", f.getName(), path);
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            fis = new FileInputStream(m_zipFile);
            if (!ftp.storeFile(f.getName(), fis)) {
                LogUtils.infof(this, "unable to store file");
                return;
            }
            LogUtils.infof(this, "finished uploading");
        } catch (final Exception e) {
            LogUtils.errorf(this, e, "Unable to FTP file to %s", m_url);
        } finally {
            IOUtils.closeQuietly(fis);
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
    }
