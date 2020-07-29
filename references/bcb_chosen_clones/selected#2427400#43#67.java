    public static Debugger getDebugger(InetAddress host, int port, String password) throws IOException {
        try {
            Socket s = new Socket(host, port);
            try {
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                int protocolVersion = in.readInt();
                if (protocolVersion > 220) {
                    throw new IOException("Incompatible protocol version " + protocolVersion + ". At most 220 was expected.");
                }
                byte[] challenge = (byte[]) in.readObject();
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(password.getBytes("UTF-8"));
                md.update(challenge);
                out.writeObject(md.digest());
                return new LocalDebuggerProxy((Debugger) in.readObject());
            } finally {
                s.close();
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new UndeclaredThrowableException(e);
        }
    }
