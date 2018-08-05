    private void readFrames(InputStream in) throws IOException, ID3v2BadParsingException, ID3v2BadHeaderException, ID3v2WrongCRCException, ID3v2DecompressionException {
        int bytes_to_read;
        if (extended_header != null) {
            int crc = extended_header.hasCRC() ? 0 : 4;
            bytes_to_read = header.getTagSize() - (extended_header.getSize() + crc) - extended_header.getPaddingSize();
        } else {
            bytes_to_read = header.getTagSize();
        }
        byte[] unsynch_frames_as_byte = null;
        try {
            unsynch_frames_as_byte = new byte[bytes_to_read];
        } catch (NegativeArraySizeException nax) {
            throw new ID3v2BadHeaderException();
        } catch (OutOfMemoryError ome) {
            throw new ID3v2BadParsingException(ome);
        }
        fillBuffer(unsynch_frames_as_byte, in);
        byte[] frames_as_byte = null;
        if (header.getUnsynchronization()) {
            frames_as_byte = synchronize(unsynch_frames_as_byte);
            if (frames_as_byte == null) {
                frames_as_byte = unsynch_frames_as_byte;
            }
        } else {
            frames_as_byte = unsynch_frames_as_byte;
        }
        if (extended_header != null && extended_header.hasCRC() == true) {
            java.util.zip.CRC32 crc_calculator = new java.util.zip.CRC32();
            crc_calculator.update(frames_as_byte);
            int crc = (int) crc_calculator.getValue();
            if ((int) crc != (int) extended_header.getCRC()) {
            }
        }
        frames = new Vector();
        ByteArrayInputStream bis = new ByteArrayInputStream(frames_as_byte);
        ID3v2Frame frame = null;
        boolean cont = true;
        while ((bis.available() > 0) && (cont == true)) {
            try {
                frame = new ID3v2Frame(bis);
            } catch (ID3v2BadFrameException e) {
                continue;
            }
            if (frame.getID() == ID3v2Frame.ID_INVALID) {
                cont = false;
            } else {
                frames.addElement(frame);
            }
        }
    }
