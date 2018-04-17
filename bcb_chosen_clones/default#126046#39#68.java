    public static void unzip() throws IOException {
        System.out.println("Extracting cache...");
        ZipFile zipfile = new ZipFile(new File((new StringBuilder()).append(dir).append("data.zip").toString()));
        Enumeration enumeration = zipfile.entries();
        do {
            if (!enumeration.hasMoreElements()) break;
            ZipEntry zipentry = (ZipEntry) enumeration.nextElement();
            DataInputStream datainputstream = new DataInputStream(zipfile.getInputStream(zipentry));
            byte abyte0[] = new byte[(int) zipentry.getSize()];
            datainputstream.readFully(abyte0);
            String s = (new StringBuilder()).append(dir).append(zipentry.getName()).toString();
            if (zipentry.isDirectory()) {
                File file = new File(s);
                file.mkdir();
            } else {
                File file1 = new File(s);
                file1.createNewFile();
                DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));
                dataoutputstream.write(abyte0);
                CRC32 crc32 = new CRC32();
                crc32.update(abyte0);
                long l = crc32.getValue();
                long l1 = zipentry.getCrc();
                if (l != l1) {
                    System.out.println((new StringBuilder()).append("CRCs differing for ").append(zipentry.getName()).toString());
                    System.out.println("May have been tampered with!");
                }
            }
        } while (true);
    }
