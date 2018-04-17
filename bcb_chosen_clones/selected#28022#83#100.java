    public void validate() throws BadCRCException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);
        try {
            dout.writeShort(m_iPacketVersion);
            dout.writeShort(m_iPacketType);
            dout.writeInt(0);
            dout.writeShort(m_iResultCode);
            dout.write(m_vBuffer);
            dout.write(m_vDummy);
            dout.close();
            byte[] vBytes = bout.toByteArray();
            CRC32 crcAlg = new CRC32();
            crcAlg.update(vBytes);
            if (!(((int) crcAlg.getValue()) == m_iCRC)) throw new BadCRCException("Bad CRC");
        } catch (IOException e) {
        }
    }
