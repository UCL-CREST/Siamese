    public static NrpePacket receivePacket(InputStream i, int padding) throws NrpeException, IOException {
        NrpePacket p = new NrpePacket();
        byte[] packet = new byte[PACKET_SIZE + padding];
        int j, k;
        for (k = 0; (j = i.read()) != -1; k++) {
            packet[k] = (byte) j;
        }
        if (k < PACKET_SIZE) {
            throw new NrpeException("Received packet is too small.  " + "Received " + k + ", expected at least " + PACKET_SIZE);
        }
        p.m_version = (short) ((positive(packet[0]) << 8) + positive(packet[1]));
        p.m_type = (short) ((positive(packet[2]) << 8) + positive(packet[3]));
        long crc_l = ((long) positive(packet[4]) << 24) + ((long) positive(packet[5]) << 16) + ((long) positive(packet[6]) << 8) + ((long) positive(packet[7]));
        p.m_resultCode = (short) ((positive(packet[8]) << 8) + positive(packet[9]));
        packet[4] = 0;
        packet[5] = 0;
        packet[6] = 0;
        packet[7] = 0;
        CRC32 crc = new CRC32();
        crc.update(packet);
        long crc_calc = crc.getValue();
        if (crc_calc != crc_l) {
            throw new NrpeException("CRC mismatch: " + crc_calc + " vs. " + crc_l);
        }
        byte[] buffer = new byte[MAX_PACKETBUFFER_LENGTH];
        System.arraycopy(packet, 10, buffer, 0, buffer.length);
        p.m_buffer = new String(buffer);
        if ((j = p.m_buffer.indexOf(0)) > 0) {
            p.m_buffer = p.m_buffer.substring(0, j);
        }
        return p;
    }
