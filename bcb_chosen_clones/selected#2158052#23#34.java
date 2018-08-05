    private void writeManifestEntry() {
        final String manifest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<!DOCTYPE manifest:manifest" + " PUBLIC \"-//OpenOffice.org//DTD Manifest 1.0//EN\" \"Manifest.dtd\" >\n" + "<manifest:manifest xmlns:manifest=\"http://openoffice.org/2001/manifest\">\n" + " <manifest:file-entry" + " manifest:media-type=\"application/vnd.sun.xml.calc\" manifest:full-path=\"/\"/>\n" + " <manifest:file-entry manifest:media-type=\"\" manifest:full-path=\"Pictures/\"/>\n" + " <manifest:file-entry manifest:media-type=\"text/xml\" manifest:full-path=\"content.xml\"/>\n" + "</manifest:manifest>\n";
        ZipEntry zipentry = new ZipEntry("meta-inf\\manifest.xml");
        zipentry.setTime(System.currentTimeMillis());
        try {
            fout.putNextEntry(zipentry);
            fout.write(manifest.getBytes());
            fout.closeEntry();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
