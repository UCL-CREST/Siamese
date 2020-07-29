    public static boolean decode(String fileName, ByteArrayOutputStream bos) throws IOException {
        byte[] buffer = new byte[BUFFERSIZE];
        int bufferLength = 0;
        BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        String line = file.readLine();
        while (line != null && !line.startsWith("=ybegin")) {
            line = file.readLine();
        }
        if (line == null) throw new IOException("Error while looking for start of a file.  Could not locate line starting with \"=ybegin\".");
        fileName = parseForName(line);
        if (fileName == null) fileName = "Unknown.blob";
        String partNo = parseForString(line, "part");
        if (partNo != null) {
            while (line != null && !line.startsWith("=ypart")) {
                line = file.readLine();
            }
            if (line == null) throw new IOException("Error while handling a multipart file.  Could not locate line starting with \"=ypart\".");
        }
        int character;
        boolean special = false;
        line = file.readLine();
        CRC32 crc = new CRC32();
        while (line != null && !line.startsWith("=yend")) {
            for (int lcv = 0; lcv < line.length(); lcv++) {
                character = (int) line.charAt(lcv);
                if (character != 61) {
                    buffer[bufferLength] = (byte) (special ? character - 106 : character - 42);
                    bufferLength++;
                    if (bufferLength == BUFFERSIZE) {
                        bos.write(buffer);
                        crc.update(buffer);
                        bufferLength = 0;
                    }
                    special = false;
                } else special = true;
            }
            line = file.readLine();
        }
        if (bufferLength > 0) {
            bos.write(buffer, 0, bufferLength);
            crc.update(buffer, 0, bufferLength);
        }
        file.close();
        Debug.debug("Size of output file = " + bos.size());
        if (line != null && line.startsWith("=yend")) {
            long fileCRC = -1;
            String crcVal = parseForString(line, "pcrc32");
            if (crcVal == null) {
                crcVal = parseForCRC(line);
            }
            if (crcVal != null) fileCRC = Long.parseLong(crcVal, 16);
            return fileCRC == crc.getValue();
        } else return false;
    }
