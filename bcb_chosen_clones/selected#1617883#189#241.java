    public static void makeLPKFile(String[] srcFilePath, String makeFilePath, LPKHeader header) {
        FileOutputStream os = null;
        DataOutputStream dos = null;
        try {
            LPKTable[] fileTable = new LPKTable[srcFilePath.length];
            long fileOffset = outputOffset(header);
            for (int i = 0; i < srcFilePath.length; i++) {
                String sourceFileName = FileUtils.getFileName(srcFilePath[i]);
                long sourceFileSize = FileUtils.getFileSize(srcFilePath[i]);
                LPKTable ft = makeLPKTable(sourceFileName, sourceFileSize, fileOffset);
                fileOffset = outputNextOffset(sourceFileSize, fileOffset);
                fileTable[i] = ft;
            }
            File file = new File(makeFilePath);
            if (!file.exists()) {
                FileUtils.makedirs(file);
            }
            os = new FileOutputStream(file);
            dos = new DataOutputStream(os);
            dos.writeInt(header.getPAKIdentity());
            writeByteArray(header.getPassword(), dos);
            dos.writeFloat(header.getVersion());
            dos.writeLong(header.getTables());
            for (int i = 0; i < fileTable.length; i++) {
                writeByteArray(fileTable[i].getFileName(), dos);
                dos.writeLong(fileTable[i].getFileSize());
                dos.writeLong(fileTable[i].getOffSet());
            }
            for (int i = 0; i < fileTable.length; i++) {
                File ftFile = new File(srcFilePath[i]);
                FileInputStream ftFis = new FileInputStream(ftFile);
                DataInputStream ftDis = new DataInputStream(ftFis);
                byte[] buff = new byte[256];
                int readLength = 0;
                while ((readLength = ftDis.read(buff)) != -1) {
                    makeBuffer(buff, readLength);
                    dos.write(buff, 0, readLength);
                }
                ftDis.close();
                ftFis.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                    dos = null;
                } catch (IOException e) {
                }
            }
        }
    }
