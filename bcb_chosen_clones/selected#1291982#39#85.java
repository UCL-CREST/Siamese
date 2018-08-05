        public void write(int b) throws IOException {
            b &= 0xff;
            if (state >= 0) {
                data[state] = (byte) b;
                state++;
                if (state == data.length) state = STATE_READCRC_1;
            } else {
                switch(state) {
                    case STATE_INVALID:
                        break;
                    case STATE_READLEN_1:
                        buffer = b;
                        state = STATE_READLEN_2;
                        break;
                    case STATE_READLEN_2:
                        buffer = (buffer << 8) + b;
                        if (Hamming16Code.isValid((int) buffer)) {
                            data = new byte[((int) buffer & 0x7ff) + 1];
                            buffer = 0;
                            state = 0;
                        } else {
                            out.close();
                            state = STATE_INVALID;
                        }
                        break;
                    case STATE_READCRC_1:
                    case STATE_READCRC_2:
                    case STATE_READCRC_3:
                    case STATE_READCRC_4:
                        buffer = (buffer << 8) + b;
                        if (state == STATE_READCRC_4) {
                            CRC32 crc = new CRC32();
                            crc.update(data);
                            if (crc.getValue() == buffer) {
                                out.write(data);
                                state = STATE_READLEN_1;
                            } else {
                                out.close();
                                state = STATE_INVALID;
                            }
                        } else {
                            state--;
                        }
                        break;
                }
            }
        }
