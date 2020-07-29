    public static void copyZip(String in, String out) throws QueryReportException {
        File fin = new File(in);
        File fout = new File(out);
        BufferedInputStream bin = null;
        ZipOutputStream zout = null;
        try {
            bin = new BufferedInputStream(new FileInputStream(fin), BUF_SIZE);
            zout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(fout)));
            zout.putNextEntry(new ZipEntry(fin.getName()));
            byte buffer[] = new byte[BUF_SIZE];
            int count = 0;
            while ((count = bin.read(buffer, 0, BUF_SIZE)) != -1) {
                zout.write(buffer, 0, count);
            }
        } catch (IOException e) {
            throw new QueryReportException(e.getMessage(), e);
        } finally {
            try {
                if (bin != null) bin.close();
                if (zout != null) zout.close();
            } catch (IOException e) {
                throw new QueryReportException(e.getMessage(), e);
            }
        }
    }
