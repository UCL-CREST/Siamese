    public static boolean YEncDecode(String fileNameIn, String fileNameOut, String yencheader) throws IOException {
        RandomAccessFile in = new RandomAccessFile(fileNameIn, "r");
        RandomAccessFile out = new RandomAccessFile(fileNameOut, "rw");
        String line = in.readLine();
        while (line != null && !line.startsWith("=ybegin ")) {
            line = in.readLine();
        }
        if (line == null) {
            in.close();
            throw new IOException("yEnc: " + fileNameOut + ": unexpected end of file");
        }
        int lineLength = Integer.parseInt(parseForName(line, "line"));
        long totalSize = Long.parseLong(parseForName(line, "size"));
        String fileCRC32 = "";
        int partSize;
        boolean success = true;
        String partNo = parseForName(line, "part");
        System.out.println(partNo);
        System.out.println("Line length: " + lineLength + ", total size: " + totalSize);
        if (partNo.equals("") == false && partNo != null) {
            System.out.println("multipart");
            while (line != null && !line.startsWith("=ypart")) {
                line = in.readLine();
            }
            if (line == null) {
                return false;
            }
            long begin = Long.parseLong(parseForName(line, "begin")) - 1;
            if (out.length() < begin) out.setLength(begin - 1);
            out.seek(begin);
            long end = Long.parseLong(parseForName(line, "end"));
            partSize = (int) (end - begin);
        } else {
            out.setLength(0);
            partSize = (int) totalSize;
        }
        boolean special = false;
        System.out.println("Line length: " + lineLength + ", part size: " + partSize);
        byte[] bufferIn = new byte[lineLength + 1];
        byte[] bufferOut = new byte[partSize];
        int byteCount = 0;
        lineLength = in.read(bufferIn);
        while (lineLength != -1 && byteCount < partSize) {
            for (int i = 0; i < lineLength; i++) {
                if (bufferIn[i] == '=') {
                    special = true;
                } else if (bufferIn[i] == 13) {
                    Thread.yield();
                    break;
                } else {
                    if (special) {
                        bufferIn[i] -= 106;
                    } else {
                        bufferIn[i] -= 42;
                    }
                    if (bufferIn[i] < 0) {
                        bufferIn[i] += 256;
                    }
                    bufferOut[byteCount] = bufferIn[i];
                    byteCount++;
                    special = false;
                }
            }
            if (byteCount == partSize) {
                break;
            }
            while (in.readByte() != 10) {
            }
            lineLength = in.read(bufferIn);
            System.out.println(new String(bufferIn));
        }
        System.out.println("last: " + (new String(bufferIn)));
        if (bufferIn[0] == '=' && bufferIn[1] == 'y' && bufferIn[2] == 'e' && bufferIn[3] == 'n' && bufferIn[4] == 'd') {
            System.out.println("yend");
            line = new String(bufferIn);
            fileCRC32 = parseForName(line, "crc32");
        }
        out.write(bufferOut);
        bufferIn = null;
        bufferOut = null;
        in.close();
        out.close();
        if (!fileCRC32.equals("")) {
            byte[] fileBuffer = new byte[(int) totalSize];
            RandomAccessFile fileCheck = new RandomAccessFile(fileNameOut, "r");
            fileCheck.readFully(fileBuffer);
            fileCheck.close();
            CRC32 crc = new CRC32();
            crc.update(fileBuffer);
            if (fileCRC32.equals(Long.toHexString(crc.getValue()).toUpperCase())) {
                System.out.println("yEnc: " + fileNameOut + " CRC32 OK");
            } else {
                success = false;
                System.out.println("Failed to confirm correctly decode " + fileNameOut + ": CRC32 not OK");
            }
        }
        System.out.println("yEnc: " + fileNameOut + ": decoding completed");
        return success;
    }
