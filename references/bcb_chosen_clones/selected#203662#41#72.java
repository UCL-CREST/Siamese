    static void invalidSlave(String msg, Socket sock) throws IOException {
        BufferedReader _sinp = null;
        PrintWriter _sout = null;
        try {
            _sout = new PrintWriter(sock.getOutputStream(), true);
            _sinp = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            _sout.println(msg);
            logger.info("NEW< " + msg);
            String txt = SocketSlaveListener.readLine(_sinp, 30);
            String sname = "";
            String spass = "";
            String shash = "";
            try {
                String[] items = txt.split(" ");
                sname = items[1].trim();
                spass = items[2].trim();
                shash = items[3].trim();
            } catch (Exception e) {
                throw new IOException("Slave Inititalization Faailed");
            }
            String pass = sname + spass + _pass;
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(pass.getBytes());
            String hash = SocketSlaveListener.hash2hex(md5.digest()).toLowerCase();
            if (!hash.equals(shash)) {
                throw new IOException("Slave Inititalization Faailed");
            }
        } catch (Exception e) {
        }
        throw new IOException("Slave Inititalization Faailed");
    }
