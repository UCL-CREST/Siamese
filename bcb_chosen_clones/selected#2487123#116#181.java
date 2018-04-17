    public void init(ConnectionManager mgr, Hashtable cfg, Socket sock) throws RemoteException {
        _cman = mgr;
        _sock = sock;
        try {
            _sout = new PrintWriter(_sock.getOutputStream(), true);
            _sinp = new BufferedReader(new InputStreamReader(_sock.getInputStream()));
            String seed = "";
            Random rand = new Random();
            for (int i = 0; i < 16; i++) {
                String hex = Integer.toHexString(rand.nextInt(256));
                if (hex.length() < 2) hex = "0" + hex;
                seed += hex.substring(hex.length() - 2);
            }
            String pass = _mpsw + seed + _spsw;
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(pass.getBytes());
            String hash = hash2hex(md5.digest()).toLowerCase();
            String banner = "INIT " + "servername" + " " + hash + " " + seed;
            sendLine(banner);
            String txt = readLine(5);
            if (txt == null) {
                throw new IOException("Slave did not send banner !!");
            }
            String sname = "";
            String spass = "";
            String sseed = "";
            try {
                String[] items = txt.split(" ");
                sname = items[1].trim();
                spass = items[2].trim();
                sseed = items[3].trim();
            } catch (Exception e) {
                SocketSlaveListener.invalidSlave("INITFAIL BadKey", _sock);
            }
            pass = _spsw + sseed + _mpsw;
            md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(pass.getBytes());
            hash = hash2hex(md5.digest()).toLowerCase();
            if (!sname.equals(_name)) {
                SocketSlaveListener.invalidSlave("INITFAIL Unknown", _sock);
            }
            if (!spass.toLowerCase().equals(hash.toLowerCase())) {
                SocketSlaveListener.invalidSlave("INITFAIL BadKey", _sock);
            }
            start();
            _cman.getSlaveManager().addSlave(_name, this, getSlaveStatus(), -1);
        } catch (IOException e) {
            if (e instanceof ConnectIOException && e.getCause() instanceof EOFException) {
                logger.info("Check slaves.xml on the master that you are allowed to connect.");
            }
            logger.info("IOException: " + e.toString());
            try {
                sock.close();
            } catch (Exception e1) {
            }
        } catch (Exception e) {
            logger.warn("Exception: " + e.toString());
            try {
                sock.close();
            } catch (Exception e2) {
            }
        }
        System.gc();
    }
