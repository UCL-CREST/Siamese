    public int addDirectory(ZipOutputStream zosFile, File dirSource, String preSource) throws Exception {
        String ldirSource = dirSource.getName();
        String lpreSource = preSource;
        int lcntEntries = 0;
        if (!(isRelativeDirectory())) {
            ldirSource = dirSource.getAbsolutePath();
            lpreSource = "";
        } else {
            ldirSource = dirSource.getName();
            lpreSource = preSource;
        }
        File[] files = dirSource.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory() && isTraverseDirectory()) {
                if (isRelativeDirectory()) {
                    lpreSource = lpreSource + File.separator + ldirSource;
                    getLog().debug("Adding directory " + lpreSource + File.separator + files[i].getName());
                    addDirectory(zosFile, files[i], lpreSource);
                    lpreSource = preSource;
                } else {
                    getLog().debug("Adding directory " + ldirSource);
                    addDirectory(zosFile, files[i], "");
                }
            } else if (!(files[i].isDirectory())) {
                try {
                    byte[] buffer = new byte[1024];
                    FileInputStream fin = new FileInputStream(files[i]);
                    if (isRelativeDirectory()) {
                        getLog().debug("Adding file [" + lpreSource + File.separator + ldirSource + File.separator + files[i].getName() + "]");
                        zosFile.putNextEntry(new ZipEntry(lpreSource + File.separator + ldirSource + File.separator + files[i].getName()));
                    } else {
                        getLog().debug("Adding file [" + ldirSource + File.separator + files[i].getName() + "]");
                        zosFile.putNextEntry(new ZipEntry(ldirSource + File.separator + files[i].getName()));
                    }
                    int length;
                    while ((length = fin.read(buffer)) > 0) {
                        zosFile.write(buffer, 0, length);
                    }
                    zosFile.closeEntry();
                    fin.close();
                } catch (IOException ioe) {
                    getLog().fatal(ioe);
                    throw ioe;
                } catch (Exception ltheXcp) {
                    getLog().fatal(ltheXcp);
                    throw ltheXcp;
                }
            }
            lcntEntries++;
        }
        return lcntEntries;
    }
