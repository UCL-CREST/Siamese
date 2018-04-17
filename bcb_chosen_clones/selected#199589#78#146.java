    private void auth() throws IOException {
        authorized = false;
        seqNumber = 0;
        DatagramSocket ds = new DatagramSocket();
        ds.setSoTimeout(UDPHID_DEFAULT_TIMEOUT);
        ds.connect(addr, port);
        DatagramPacket p = new DatagramPacket(buffer.array(), buffer.capacity());
        for (int i = 0; i < UDPHID_DEFAULT_ATTEMPTS; i++) {
            buffer.clear();
            buffer.put((byte) REQ_CHALLENGE);
            buffer.put(htons((short) UDPHID_PROTO));
            buffer.put(name.getBytes());
            ds.send(new DatagramPacket(buffer.array(), buffer.position()));
            buffer.clear();
            try {
                ds.receive(p);
            } catch (SocketTimeoutException e) {
                continue;
            }
            switch(buffer.get()) {
                case ANS_CHALLENGE:
                    break;
                case ANS_FAILURE:
                    throw new IOException("REQ_FAILURE");
                default:
                    throw new IOException("invalid packet");
            }
            byte challenge_id = buffer.get();
            int challenge_len = (int) buffer.get();
            byte[] challenge = new byte[challenge_len];
            buffer.get(challenge, 0, p.getLength() - buffer.position());
            byte[] response;
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(challenge_id);
                md.update(password.getBytes(), 0, password.length());
                md.update(challenge, 0, challenge.length);
                response = md.digest();
            } catch (NoSuchAlgorithmException e) {
                throw new IOException("NoSuchAlgorithmException: " + e.toString());
            }
            buffer.clear();
            buffer.put((byte) REQ_RESPONSE);
            buffer.put(challenge_id);
            buffer.put((byte) response.length);
            buffer.put(response);
            buffer.put(login.getBytes());
            ds.send(new DatagramPacket(buffer.array(), buffer.position()));
            buffer.clear();
            try {
                ds.receive(p);
            } catch (SocketTimeoutException e) {
                continue;
            }
            switch(buffer.get()) {
                case ANS_SUCCESS:
                    int sidLength = buffer.get();
                    sid = new byte[sidLength];
                    buffer.get(sid, 0, sidLength);
                    authorized = true;
                    return;
                case ANS_FAILURE:
                    throw new IOException("access deny");
                default:
                    throw new IOException("invalid packet");
            }
        }
        throw new IOException("operation time out");
    }
