    public byte[] buildPacket(int padding, SecureRandom random) {
        byte[] packet = new byte[PACKET_SIZE + padding];
        byte[] buffer = m_buffer.getBytes();
        if (random != null) {
            random.nextBytes(packet);
        } else {
            for (int i = 10 + buffer.length; i < packet.length; i++) {
                packet[i] = 0;
            }
        }
        packet[0] = (byte) ((m_version >> 8) & 0xff);
        packet[1] = (byte) (m_version & 0xff);
        packet[2] = (byte) ((m_type >> 8) & 0xff);
        packet[3] = (byte) (m_type & 0xff);
        packet[4] = 0;
        packet[5] = 0;
        packet[6] = 0;
        packet[7] = 0;
        packet[8] = (byte) ((m_resultCode >> 8) & 0xff);
        packet[9] = (byte) (m_resultCode & 0xff);
        System.arraycopy(buffer, 0, packet, 10, buffer.length);
        if ((10 + buffer.length) < PACKET_SIZE - 1) {
            packet[10 + buffer.length] = 0;
        }
        packet[PACKET_SIZE - 1] = 0;
        CRC32 crc = new CRC32();
        crc.update(packet);
        long crc_l = crc.getValue();
        packet[4] = (byte) ((crc_l >> 24) & 0xff);
        packet[5] = (byte) ((crc_l >> 16) & 0xff);
        packet[6] = (byte) ((crc_l >> 8) & 0xff);
        packet[7] = (byte) (crc_l & 0xff);
        return packet;
    }
