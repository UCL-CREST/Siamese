    private File generateZip(Preference preference, File xml, List<Document> documents) {
        File zfile = new File(preference.getStoreLocation() + File.separator + AppConstants.DMS_ZIPFILE);
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zfile));
            FileInputStream in = new FileInputStream(xml);
            out.putNextEntry(new ZipEntry(xml.getName()));
            int len;
            byte[] buf = new byte[1024];
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
            for (Iterator<Document> iter = documents.iterator(); iter.hasNext(); ) {
                Document document = iter.next();
                in = new FileInputStream(preference.getStoreLocation() + File.separator + document.getName());
                out.putNextEntry(new ZipEntry(document.getName()));
                len = 0;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            zfile = null;
        }
        return zfile;
    }
