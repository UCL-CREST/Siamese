    public BufferedWriter createWriter(File outfile, String encoding) throws UnsupportedEncodingException, IOException {
        int k_blockSize = 1024;
        int byteCount;
        char[] buf = new char[k_blockSize];
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outfile));
        zos.setMethod(ZipOutputStream.DEFLATED);
        OutputStreamWriter osw = new OutputStreamWriter(zos, "ISO-8859-1");
        BufferedWriter bw = new BufferedWriter(osw);
        ZipEntry zot;
        ZipInputStream zis = new ZipInputStream(new FileInputStream(infile));
        InputStreamReader isr = new InputStreamReader(zis, "ISO-8859-1");
        BufferedReader br = new BufferedReader(isr);
        ZipEntry zit;
        while ((zit = zis.getNextEntry()) != null) {
            if (zit.getName().equals("content.xml")) {
                continue;
            }
            zot = new ZipEntry(zit.getName());
            zos.putNextEntry(zot);
            while ((byteCount = br.read(buf, 0, k_blockSize)) >= 0) bw.write(buf, 0, byteCount);
            bw.flush();
            zos.closeEntry();
        }
        zos.putNextEntry(new ZipEntry("content.xml"));
        bw.flush();
        return new BufferedWriter(new OutputStreamWriter(zos, "UTF-8"));
    }
