    public static Message create(ByteBuffer bb) {
        Message msg = null;
        try {
            byte[] hdr = new byte[LENGTH];
            bb.rewind();
            bb.get(hdr);
            Class msgClass = s_dict[hdr[CMD_OFFSET]];
            if (msgClass == null) {
                Logging.logger.warning("Unrecognized message " + hdr[CMD_OFFSET]);
            } else {
                Class[] cstrArgs = new Class[1];
                cstrArgs[0] = Class.forName("java.nio.ByteBuffer");
                Constructor cstr = msgClass.getConstructor(cstrArgs);
                msg = (Message) cstr.newInstance(bb);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        return msg;
    }
