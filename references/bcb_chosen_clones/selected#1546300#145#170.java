    public void saveZipDocument(OutputStream out, boolean includeMedia) throws Exception {
        ZipOutputStream zos = new ZipOutputStream(out);
        zos.putNextEntry(new ZipEntry(name + ".jclic"));
        saveDocument(zos);
        zos.closeEntry();
        if (includeMedia) {
            Set set = Collections.synchronizedSet(new HashSet());
            Iterator it = mediaBag.getElements().iterator();
            while (it.hasNext()) {
                MediaBagElement mbe = (MediaBagElement) it.next();
                if (mbe.saveFlag) {
                    String fName = mbe.getFileName();
                    if (fName != null && fName.length() > 0) set.add(fName);
                }
            }
            it = set.iterator();
            while (it.hasNext()) {
                String fName = (String) it.next();
                zos.putNextEntry(new ZipEntry(fName));
                zos.write(fileSystem.getBytes(fName));
                zos.closeEntry();
            }
        }
        zos.close();
        out.close();
    }
