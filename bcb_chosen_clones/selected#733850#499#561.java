    protected void putUninstaller() throws Exception {
        String uninstallerCondition = idata.info.getUninstallerCondition();
        if ((uninstallerCondition != null) && (uninstallerCondition.length() > 0) && !this.rules.isConditionTrue(uninstallerCondition)) {
            Debug.log("Uninstaller has a condition (" + uninstallerCondition + ") which is not fulfilled.");
            Debug.log("Skipping creation of uninstaller.");
            return;
        }
        InputStream[] in = new InputStream[2];
        in[0] = UnpackerBase.class.getResourceAsStream("/res/IzPack.uninstaller");
        if (in[0] == null) {
            return;
        }
        in[1] = UnpackerBase.class.getResourceAsStream("/res/IzPack.uninstaller-ext");
        String dest = IoHelper.translatePath(idata.info.getUninstallerPath(), vs);
        String jar = dest + File.separator + idata.info.getUninstallerName();
        File pathMaker = new File(dest);
        pathMaker.mkdirs();
        udata.setUninstallerJarFilename(jar);
        udata.setUninstallerPath(dest);
        FileOutputStream out = new FileOutputStream(jar);
        BufferedOutputStream bos = new BufferedOutputStream(out);
        ZipOutputStream outJar = new ZipOutputStream(bos);
        idata.uninstallOutJar = outJar;
        outJar.setLevel(9);
        udata.addFile(jar, true);
        HashSet<String> doubles = new HashSet<String>();
        for (InputStream anIn : in) {
            if (anIn == null) {
                continue;
            }
            ZipInputStream inRes = new ZipInputStream(anIn);
            ZipEntry zentry = inRes.getNextEntry();
            while (zentry != null) {
                if (!doubles.contains(zentry.getName())) {
                    doubles.add(zentry.getName());
                    outJar.putNextEntry(new ZipEntry(zentry.getName()));
                    int unc = inRes.read();
                    while (unc != -1) {
                        outJar.write(unc);
                        unc = inRes.read();
                    }
                    inRes.closeEntry();
                    outJar.closeEntry();
                }
                zentry = inRes.getNextEntry();
            }
            inRes.close();
        }
        if (idata.info.isPrivilegedExecutionRequired()) {
            if (System.getenv("izpack.mode") != null && System.getenv("izpack.mode").equals("privileged")) {
                outJar.putNextEntry(new ZipEntry("exec-admin"));
                outJar.closeEntry();
            }
        }
        InputStream in2 = Unpacker.class.getResourceAsStream("/langpacks/" + idata.localeISO3 + ".xml");
        outJar.putNextEntry(new ZipEntry("langpack.xml"));
        int read = in2.read();
        while (read != -1) {
            outJar.write(read);
            read = in2.read();
        }
        outJar.closeEntry();
    }
