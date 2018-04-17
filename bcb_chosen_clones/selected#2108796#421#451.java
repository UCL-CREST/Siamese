    private void handleServerIntroduction(DataPacket serverPacket) {
        DataPacketIterator iter = serverPacket.getDataPacketIterator();
        String version = iter.nextString();
        int serverReportedUDPPort = iter.nextUByte2();
        _authKey = iter.nextUByte4();
        _introKey = iter.nextUByte4();
        _clientKey = makeClientKey(_authKey, _introKey);
        String passwordKey = iter.nextString();
        _logger.log(Level.INFO, "Connection to version " + version + " with udp port " + serverReportedUDPPort);
        DataPacket packet = null;
        if (initUDPSocketAndStartPacketReader(_tcpSocket.getInetAddress(), serverReportedUDPPort)) {
            ParameterBuilder builder = new ParameterBuilder();
            builder.appendUByte2(_udpSocket.getLocalPort());
            builder.appendString(_user);
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ignore) {
            }
            md5.update(_serverKey.getBytes());
            md5.update(passwordKey.getBytes());
            md5.update(_password.getBytes());
            for (byte b : md5.digest()) {
                builder.appendByte(b);
            }
            packet = new DataPacketImpl(ClientCommandConstants.INTRODUCTION, builder.toParameter());
        } else {
            packet = new DataPacketImpl(ClientCommandConstants.TCP_ONLY);
        }
        sendTCPPacket(packet);
    }
