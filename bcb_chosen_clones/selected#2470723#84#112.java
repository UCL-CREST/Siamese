    public void generateKMZ(File kmzFile, Route r) {
        byte[] buf = new byte[2048];
        try {
            kmzFile.createNewFile();
            ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(kmzFile));
            zipFile.putNextEntry(new ZipEntry("doc.kml"));
            writeKml(zipFile, r);
            zipFile.closeEntry();
            ListIterator<Photo> iter = r.getPhotos().listIterator();
            while (iter.hasNext()) {
                String p = iter.next().getPath();
                if (p != null) {
                    File f = new File(p);
                    String name = "files/" + f.getName();
                    zipFile.putNextEntry(new ZipEntry(name));
                    FileInputStream in = new FileInputStream(f);
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        zipFile.write(buf, 0, len);
                    }
                    zipFile.closeEntry();
                }
            }
            zipFile.flush();
            zipFile.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
