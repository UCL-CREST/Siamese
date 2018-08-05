    private void createZipFile(String profileName, ArrayList<String> fileList, String zipFileName) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        FileInputStream in = null;
        try {
            for (String s : fileList) {
                byte[] tmpBuf = new byte[1024];
                in = new FileInputStream(s);
                String zipEntryName = cleanZipEntry(profileName, s);
                out.putNextEntry(new ZipEntry(zipEntryName));
                int len;
                while ((len = in.read(tmpBuf)) > 0) {
                    out.write(tmpBuf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (Exception e) {
            MiscUtils.stackTrace2String(e);
            throw e;
        } finally {
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }
