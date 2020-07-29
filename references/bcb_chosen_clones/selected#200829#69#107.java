    public PluginResult check(boolean ssl, String hostname, int port, int timeout, String cmdline) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        byte[] packet = new byte[2 + 2 + 4 + 2 + MAX_PACKETBUFFER_LENGTH + C_STRUCT_PADDING];
        htons(NRPE_PACKET_VERSION_2, packet, 0);
        htons(QUERY_PACKET, packet, 2);
        htonl(0, packet, 4);
        htons(PluginResult.STATE_OK, packet, 8);
        System.arraycopy(cmdline.getBytes(), 0, packet, 10, cmdline.length());
        packet[10 + MAX_PACKETBUFFER_LENGTH - 1] = 0;
        CRC32 crc32 = new CRC32();
        crc32.update(packet);
        htonl(crc32.getValue(), packet, 4);
        hexdump(packet, 0, packet.length, System.out);
        Socket socket = new Socket(hostname, port);
        socket.setSoTimeout(timeout);
        if (ssl) {
            socket = wrap(socket);
        }
        OutputStream out = socket.getOutputStream();
        out.write(packet);
        InputStream in = socket.getInputStream();
        int length = in.read(packet);
        System.out.println("length=" + length);
        hexdump(packet, 0, length, System.out);
        long packetCrc32 = ntohl(packet, 4);
        htonl(0, packet, 4);
        crc32.reset();
        crc32.update(packet);
        if (packetCrc32 != crc32.getValue()) {
            throw new IOException("Bad CRC32 " + packetCrc32 + " (expected " + crc32.getValue() + ")");
        }
        PluginResult result = new PluginResult();
        result.setState(ntohs(packet, 8));
        StringBuffer pluginOutput = new StringBuffer();
        for (int i = 10; i < packet.length && 0 != packet[i]; i++) {
            pluginOutput.append((char) packet[i]);
        }
        result.setPluginOutput(pluginOutput.toString());
        return result;
    }
