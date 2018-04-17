    public boolean retrieveCertificate(String host, int port, String user, String password, int lifetime) {
        if (host == null) {
            host = DEFAULTSERVER;
        }
        if (port == 0) {
            port = PORT;
        }
        if (lifetime == 0) {
            lifetime = DEFAULTLIFETIME;
        }
        try {
            MyProxy proxy = new MyProxy(host, port);
            GSSCredential cred = proxy.get(user, password, lifetime);
            int remainingtime = cred.getRemainingLifetime();
            String tmpdir = System.getProperty("java.io.tmpdir");
            ProcessBuilder pb = new ProcessBuilder("id", "-u");
            Process p = pb.start();
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String uid = input.readLine();
            String outputFile = tmpdir + "/" + PROXY_FILE_PREFIX + uid;
            File f = new File(outputFile);
            if (f.exists()) {
                f.delete();
            }
            String path = f.getPath();
            OutputStream out = null;
            try {
                out = new FileOutputStream(f);
                byte[] data = ((ExtendedGSSCredential) cred).export(ExtendedGSSCredential.IMPEXP_OPAQUE);
                out.write(data);
            } catch (Exception e) {
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                    }
                }
            }
            pb = new ProcessBuilder("chmod", "600", outputFile);
            pb.start();
            cred.dispose();
            log.info("A proxy certificate has been received for user " + user + " in " + path);
            log.info("valid remaining time in second(s): " + remainingtime);
            return true;
        } catch (Exception e) {
            log.info("Failed to retrieve proxy certificate!!\n" + e);
            return false;
        }
    }
