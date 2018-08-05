    @SuppressWarnings("unchecked")
    private void traverseZipFile(String zipFileName, XMLReader xmlReader, FileWriter fw) throws SAXException, IOException {
        FileInputStream fileIn = null;
        BufferedInputStream bufIn = null;
        ZipInputStream zipIn = null;
        try {
            fileIn = new FileInputStream(zipFileName);
            bufIn = new BufferedInputStream(fileIn);
            zipIn = new ZipInputStream(bufIn);
            ZipEntry entry = null;
            while ((entry = zipIn.getNextEntry()) != null) {
                if (entry.isDirectory()) continue;
                if (entry.getName().endsWith(".DS_Store")) {
                    System.out.println(entry.getName());
                    int len = zipIn.read(new byte[zipIn.available()]);
                    continue;
                } else if (entry.getName().endsWith(".xml")) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte buf[] = new byte[500000];
                    int blen;
                    while ((blen = zipIn.read(buf)) != -1) {
                        bos.write(buf, 0, blen);
                    }
                    byte cbuf[] = bos.toByteArray();
                    ByteArrayInputStream bis = new ByteArrayInputStream(cbuf);
                    InputSource inputSource = new InputSource(bis);
                    xmlReader.parse(inputSource);
                    ucid_flag = 0;
                    msid_flag = 0;
                    ucid.delete(0, ucid.length());
                    msid.delete(0, msid.length());
                    index = 1;
                    paraLength = 0;
                    ucidTag_flag = 0;
                    msidTag_flag = 0;
                } else continue;
            }
        } finally {
            zipIn.close();
            bufIn.close();
            fileIn.close();
        }
    }
