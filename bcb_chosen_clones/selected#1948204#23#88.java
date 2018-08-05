    public boolean install(InstallMonitor moni) throws AbortInstallException {
        installedName = Info.getSystemActions().getTargetName(location, target);
        backupFile(moni, installedName);
        OutputStream os = Info.getSystemActions().openOutputFile(location, target);
        if (os == null) return false;
        long bytes_written = 0;
        try {
            InputStream is = getClass().getResourceAsStream("/Uninstall.class");
            if (is != null) {
                byte[] buffer = new byte[4096];
                int bytes = 0;
                while ((bytes = is.read(buffer)) >= 0) {
                    os.write(buffer, 0, bytes);
                    bytes_written += bytes;
                }
            }
        } catch (Exception e) {
            System.err.println("can not write installer zip : " + e);
            System.err.println("last file was Uninstall.class");
            e.printStackTrace();
            return false;
        }
        ZipOutputStream zips = new ZipOutputStream(os, bytes_written);
        zips.setLevel(9);
        InstallableFile insf = null;
        long count = 0;
        try {
            Enumeration instEn = container.getAllInstallables();
            while (instEn.hasMoreElements()) {
                Installable inst = (Installable) (instEn.nextElement());
                if (!(inst instanceof InstallableFile)) continue;
                insf = (InstallableFile) inst;
                if (!insf.getPackage().equals("installer")) continue;
                InputStream is = Info.getInstallationSource().getFile(insf.getName(), insf.getLocation());
                if (is == null) {
                    System.err.println("can not get installer component " + insf.getName());
                    return false;
                }
                ZipEntry ze = new ZipEntry(insf.getName());
                zips.putNextEntry(ze);
                byte[] buffer = new byte[4096];
                int bytes = 0;
                count = 0;
                try {
                    while ((bytes = is.read(buffer)) >= 0) {
                        count += bytes;
                        zips.write(buffer, 0, bytes);
                    }
                } catch (EOFException e) {
                }
                is.close();
            }
            wasInstalled = true;
            ZipEntry ze = new ZipEntry("uninstall.dat");
            zips.putNextEntry(ze);
            Info.saveState(zips);
            zips.finish();
        } catch (Exception e) {
            System.err.println("can not write installer zip : " + e);
            System.err.println("last file was " + insf);
            System.err.println("got " + count + " bytes from that file\n");
            e.printStackTrace();
            return false;
        }
        return true;
    }
