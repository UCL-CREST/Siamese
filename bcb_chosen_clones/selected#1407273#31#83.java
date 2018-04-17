    public void createZippedFile(String[] fileNameList, String dirName, String outputZippedFileName) throws Exception {
        ZipOutputStream zipOutput = null;
        FileInputStream inputFile = null;
        try {
            if (fileNameList.length <= 0) {
                throw new Exception("Passed fileNameList is empty!");
            }
            FileOutputStream out = new FileOutputStream(new File(outputZippedFileName));
            zipOutput = new ZipOutputStream(out);
            ZipEntry tmpZipEntry = null;
            CRC32 crc = new CRC32();
            for (int i = 0; i < fileNameList.length; i++) {
                String tmpFile = fileNameList[i];
                File tmpFileHandle = new File(dirName + fileSeparator + tmpFile);
                inputFile = new FileInputStream(tmpFileHandle);
                if (!tmpFileHandle.exists()) {
                    throw new Exception("One or more files specified in fileNameList do not exist!");
                }
                if (!tmpFileHandle.isFile()) {
                    throw new Exception("One or more files specified in fileNameList is not a normal file!");
                }
                if (!tmpFileHandle.canRead()) {
                    throw new Exception("One or more files specified in fileNameList is not readable!");
                }
                int fileSize = (int) tmpFileHandle.length();
                byte[] b = new byte[fileSize];
                crc.reset();
                int bytesRead = 0;
                while (fileSize > 0 && ((bytesRead = inputFile.read(b)) != -1)) {
                    crc.update(b, 0, bytesRead);
                }
                tmpZipEntry = new ZipEntry(tmpFile);
                tmpZipEntry.setMethod(ZipEntry.STORED);
                tmpZipEntry.setCompressedSize(tmpFileHandle.length());
                tmpZipEntry.setSize(tmpFileHandle.length());
                tmpZipEntry.setCrc(crc.getValue());
                zipOutput.putNextEntry(tmpZipEntry);
                zipOutput.write(b);
                zipOutput.flush();
                inputFile.close();
            }
            zipOutput.close();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (zipOutput != null) {
                zipOutput.close();
            }
            if (inputFile != null) {
                inputFile.close();
            }
        }
    }
