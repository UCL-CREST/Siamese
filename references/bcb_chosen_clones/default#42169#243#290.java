    private void traverseTarGzFile(String zipFileName, XMLReader xmlReader, FileWriter fw) throws IOException {
        index = 1;
        paraLength = 0;
        FileInputStream fileIn = null;
        BufferedInputStream bufIn = null;
        GZIPInputStream gzipIn = null;
        TarInputStream taris = null;
        try {
            fileIn = new FileInputStream(zipFileName);
            bufIn = new BufferedInputStream(fileIn);
            gzipIn = new GZIPInputStream(bufIn);
            taris = new TarInputStream(gzipIn);
            TarEntry entry = null;
            while ((entry = taris.getNextEntry()) != null) {
                if (entry.isDirectory()) continue;
                if (entry.getName().endsWith(".DS_Store")) {
                    System.out.println(entry.getName());
                    continue;
                } else if (entry.getName().endsWith(".xml")) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte buf[] = new byte[50000];
                    int blen;
                    while ((blen = taris.read(buf)) != -1) {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            taris.close();
            gzipIn.close();
            bufIn.close();
            fileIn.close();
        }
    }
