    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        byte buf[] = new byte[MAX_PACKETBUFFER_LENGTH];
        byte command[] = ((String) message).getBytes("US-ASCII");
        buf[0] = 0;
        buf[1] = PACKET_VERSION;
        buf[2] = 0;
        buf[3] = QUERY_PACKET;
        System.arraycopy(command, 0, buf, 10, command.length);
        CRC32 crc = new CRC32();
        crc.update(buf);
        long check = crc.getValue();
        System.arraycopy(toUnsignedInt(check), 0, buf, 4, 4);
        IoBuffer wrap = IoBuffer.wrap(buf);
        out.write(wrap);
    }
