    private boolean isNotCorrupted(String fileName) throws Exception {
        FileReader xmlFile;
        FileReader checksumFile;
        CRC32 check = new CRC32();
        int c;
        String compareCheck = "";
        check.reset();
        try {
            xmlFile = new FileReader(fileName);
            while ((c = xmlFile.read()) != -1) {
                check.update((char) c);
            }
        } catch (FileNotFoundException e) {
            System.out.println("[Collector] [ERROR] was not possible to open xml file: " + fileName);
            throw e;
        } catch (IOException e) {
            System.out.println("[Collector] [ERROR] was not possible to read xml file: " + fileName);
            throw e;
        }
        try {
            checksumFile = new FileReader(fileName.substring(0, fileName.length() - 4) + ".crc32");
            while ((c = checksumFile.read()) != -1) {
                compareCheck += (char) c;
            }
        } catch (FileNotFoundException e) {
            System.out.println("[Collector] [ERROR] was not possible to open checksum file: " + fileName.substring(0, fileName.length() - 4) + ".crc32");
            throw e;
        } catch (IOException e) {
            System.out.println("[Collector] [ERROR] was not possible to read checksum file: " + fileName.substring(0, fileName.length() - 4) + ".crc32");
            throw e;
        }
        return compareCheck.trim().equalsIgnoreCase(String.valueOf(check.getValue()));
    }
