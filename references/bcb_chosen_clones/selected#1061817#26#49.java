    public void createZip(String _filesToZip[], String _targetZip) {
        byte[] buffer = new byte[18024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(_targetZip));
            out.setLevel(Deflater.DEFAULT_COMPRESSION);
            for (int i = 0; i < _filesToZip.length; i++) {
                FileInputStream in = new FileInputStream(_filesToZip[i]);
                out.putNextEntry(new ZipEntry(_filesToZip[i]));
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
