    public boolean execute_check() {
        FTPClient ftp = new FTPClient();
        File filename = null;
        FileChannel channel;
        InputStream is;
        OutputStream os;
        int reply;
        if (super.verbose > 0) verbose = true;
        ftp.setDefaultPort(port);
        ftp.setDefaultTimeout(timeout);
        if (verbose) {
            System.out.println("Using FTP Server: " + hostname);
            System.out.println("Using FTP Port: " + port);
            System.out.println("Using Timeout of: " + timeout);
        }
        if (passive) {
            ftp.enterLocalPassiveMode();
            if (verbose) System.out.println("Using Passive Mode");
        }
        try {
            filename = new File(file);
            channel = new RandomAccessFile(filename, "rw").getChannel();
            if (verbose) System.out.println("Attempting FTP Connection to " + hostname);
            ftp.connect(hostname);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                if (verbose) System.out.println("FTP Connection to " + hostname + " failed");
                check_state = common_h.STATE_CRITICAL;
                check_message = ftp.getReplyString();
                filename.delete();
                ftp.disconnect();
                return true;
            }
            if (username != null && password != null) {
                if (verbose) System.out.println("Attempting to log in into FTP Server " + hostname);
                if (!ftp.login(username, password)) {
                    if (verbose) System.out.println("Unable to log in to FTP Server " + hostname);
                    check_state = common_h.STATE_CRITICAL;
                    check_message = ftp.getReplyString();
                    ftp.disconnect();
                    filename.delete();
                    return true;
                }
            }
            if (verbose) System.out.println("Attempting to change to required directory");
            if (!ftp.changeWorkingDirectory(directory)) {
                if (verbose) System.out.println("Required directory cannot be found!");
                check_state = common_h.STATE_WARNING;
                check_message = ftp.getReplyString();
                ftp.disconnect();
                filename.delete();
                return true;
            }
            if (verbose) System.out.println("Attempting to retrieve specified file!");
            is = ftp.retrieveFileStream(file);
            if (is == null) {
                if (verbose) System.out.println("Unable to locate required file.");
                check_state = common_h.STATE_WARNING;
                check_message = ftp.getReplyString();
                ftp.disconnect();
                filename.delete();
                return true;
            }
            os = Channels.newOutputStream(channel);
            byte[] buf = new byte[4096];
            if (verbose) System.out.println("Beginning File transfer...");
            for (int len = -1; (len = is.read(buf)) != -1; ) os.write(buf, 0, len);
            if (verbose) {
                System.out.println("...transfer complete.");
                System.out.println("Attempting to finalise Command");
            }
            if (!ftp.completePendingCommand()) {
                if (verbose) System.out.println("Unable to finalise command");
                check_state = common_h.STATE_WARNING;
                check_message = ftp.getReplyString();
                ftp.disconnect();
                filename.delete();
                return true;
            }
            if (verbose) System.out.println("Check Completed.");
            check_state = common_h.STATE_OK;
            check_message = ftp.getReplyString();
            is.close();
            os.close();
            channel.close();
            filename.delete();
        } catch (IOException e) {
            check_state = common_h.STATE_CRITICAL;
            check_message = e.getMessage();
            if (filename != null) filename.delete();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.logout();
                    ftp.disconnect();
                } catch (Exception e) {
                }
            }
        }
        return true;
    }
