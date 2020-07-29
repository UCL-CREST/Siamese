    public GEnterprisePackage write(GEnterprisePackage pkg) throws Exception {
        String suffix = "_ims.xml";
        if (zipped) {
            suffix = "_ims.zip";
        }
        File f = null;
        if (appendUnique) {
            f = File.createTempFile(filename, suffix, new File(this.directory));
        } else {
            f = new File(this.directory + File.separator + filename + suffix);
        }
        if (zipped) {
            FileOutputStream bout = new FileOutputStream(f);
            ZipOutputStream zipout = new ZipOutputStream(bout);
            ZipEntry ze = new ZipEntry(filename + "_ims.xml");
            zipout.putNextEntry(ze);
            pkg.write(zipout);
            zipout.closeEntry();
            zipout.close();
        } else {
            FileOutputStream out = new FileOutputStream(f);
            pkg.write(out);
            out.close();
        }
        return new GEnterprisePackage();
    }
