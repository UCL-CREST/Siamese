    public static String send(String ipStr, int port, String password, String command, InetAddress localhost, int localPort) throws SocketTimeoutException, BadRcon, ResponseEmpty {
        StringBuffer response = new StringBuffer();
        try {
            rconSocket = new Socket();
            rconSocket.bind(new InetSocketAddress(localhost, localPort));
            rconSocket.connect(new InetSocketAddress(ipStr, port), RESPONSE_TIMEOUT);
            out = rconSocket.getOutputStream();
            in = rconSocket.getInputStream();
            BufferedReader buffRead = new BufferedReader(new InputStreamReader(in));
            rconSocket.setSoTimeout(RESPONSE_TIMEOUT);
            String digestSeed = "";
            boolean loggedIn = false;
            boolean keepGoing = true;
            while (keepGoing) {
                String receivedContent = buffRead.readLine();
                if (receivedContent.startsWith("### Digest seed: ")) {
                    digestSeed = receivedContent.substring(17, receivedContent.length());
                    try {
                        MessageDigest md5 = MessageDigest.getInstance("MD5");
                        md5.update(digestSeed.getBytes());
                        md5.update(password.getBytes());
                        String digestStr = "login " + digestedToHex(md5.digest()) + "\n";
                        out.write(digestStr.getBytes());
                    } catch (NoSuchAlgorithmException e1) {
                        response.append("MD5 algorithm not available - unable to complete RCON request.");
                        keepGoing = false;
                    }
                } else if (receivedContent.startsWith("error: not authenticated: you can only invoke 'login'")) {
                    throw new BadRcon();
                } else if (receivedContent.startsWith("Authentication failed.")) {
                    throw new BadRcon();
                } else if (receivedContent.startsWith("Authentication successful, rcon ready.")) {
                    keepGoing = false;
                    loggedIn = true;
                }
            }
            if (loggedIn) {
                String cmd = "exec " + command + "\n";
                out.write(cmd.getBytes());
                readResponse(buffRead, response);
                if (response.length() == 0) {
                    throw new ResponseEmpty();
                }
            }
        } catch (SocketTimeoutException timeout) {
            throw timeout;
        } catch (UnknownHostException e) {
            response.append("UnknownHostException: " + e.getMessage());
        } catch (IOException e) {
            response.append("Couldn't get I/O for the connection: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (rconSocket != null) {
                    rconSocket.close();
                }
            } catch (IOException e1) {
            }
        }
        return response.toString();
    }
