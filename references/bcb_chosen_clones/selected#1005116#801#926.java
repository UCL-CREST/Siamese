    private File extractUninstallFiles(File _destPath, boolean upgrade, String lastVer) {
        File oldlog = null;
        try {
            boolean oldClassCopied = false;
            File destPath = new File(_destPath, "vai_" + VAGlobals.APP_NAME + "_" + VAGlobals.APP_VERSION);
            if (upgrade) {
                File lastVerPath = new File(_destPath, "vai_" + VAGlobals.APP_NAME + "_" + lastVer);
                if (destPath.equals(lastVerPath)) {
                    File bkdir = new File(destPath.getAbsolutePath() + ".bak");
                    if (!destPath.renameTo(bkdir)) {
                        throw new IOException(VAGlobals.i18n("Setup_NotCreateDirectory") + " " + destPath);
                    }
                    oldlog = new File(bkdir.getAbsolutePath() + System.getProperty("file.separator") + "uninstall.vai");
                    lastVerPath = bkdir;
                } else {
                    oldlog = new File(lastVerPath.getAbsolutePath() + System.getProperty("file.separator") + "uninstall.vai");
                }
                if ((!destPath.exists()) && (!destPath.mkdirs())) {
                    throw new IOException(VAGlobals.i18n("Setup_NotCreateDirectory") + " " + destPath);
                }
                if (uInfo_.module) oldClassCopied = copyOldSetupClass(lastVerPath, destPath);
            } else {
                if ((!destPath.exists()) && (!destPath.mkdirs())) {
                    throw new IOException(VAGlobals.i18n("Setup_NotCreateDirectory") + " " + destPath);
                }
            }
            dirty_ = true;
            File[] ls = destPath.listFiles();
            for (int i = 0; i < ls.length; i++) {
                if (!oldClassCopied) ls[i].delete(); else if (!ls[i].getPath().equals(destPath.getAbsolutePath() + File.separator + installClassName_ + ".class")) ls[i].delete();
            }
            byte[] buf = new byte[0];
            int read = 0;
            if (!oldClassCopied && (installClassSize_ > 0 || jarOffset_ > 0)) {
                final File outClassFile = new File(destPath.getAbsolutePath() + File.separator + installClassName_ + ".class");
                if (outClassFile.exists() && !outClassFile.delete()) {
                    ui_.showError(new Exception(VAGlobals.i18n("Setup_FileNotCreated") + ":\n" + outClassFile.getName()));
                }
                final FileOutputStream out = new FileOutputStream(outClassFile);
                final FileInputStream in = new FileInputStream(fileWithArchive_);
                if (installClassOffset_ > 0) {
                    in.skip(installClassOffset_);
                }
                buf = new byte[0];
                if (installClassSize_ < 0) buf = new byte[(int) jarOffset_]; else buf = new byte[(int) installClassSize_];
                read = in.read(buf, 0, buf.length);
                out.write(buf, 0, read);
                out.close();
                in.close();
            }
            final FileInputStream in = new FileInputStream(fileWithArchive_);
            if (jarOffset_ > 0) {
                in.skip(jarOffset_);
            }
            JarInputStream jar = new JarInputStream(in);
            final File outJarFile = new File(destPath.getAbsolutePath() + File.separator + "install.jar");
            if (outJarFile.exists() && !outJarFile.delete()) {
                ui_.showError(new Exception(VAGlobals.i18n("Setup_FileNotCreated") + ":\n" + outJarFile.getName()));
            }
            JarOutputStream outJar = new JarOutputStream(new FileOutputStream(outJarFile));
            ZipEntry entry = jar.getNextEntry();
            final int bufSize = 32768;
            buf = new byte[bufSize];
            while (entry != null) {
                String entryName = entry.getName();
                if (entryName.equals("com/memoire/vainstall/resources/vainstall.properties")) {
                } else if (entryName.equals(installClassName_ + ".class") && !oldClassCopied) {
                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(destPath.getAbsolutePath() + File.separator + installClassName_ + ".class");
                        VAGlobals.copyStream(jar, out, buf);
                    } catch (IOException e) {
                        throw e;
                    } finally {
                        if (out != null) out.close();
                    }
                } else if (!entryName.endsWith(".zip")) {
                    if (VAGlobals.DEBUG) VAGlobals.printDebug("jar entry name " + entryName);
                    ZipEntry outEntry = new ZipEntry(entryName);
                    CRC32 crc = new CRC32();
                    outJar.putNextEntry(outEntry);
                    int size = 0;
                    while ((read = jar.read(buf, 0, bufSize)) >= 0) {
                        size += read;
                        if (read == 0) {
                            Thread.yield();
                        } else {
                            outJar.write(buf, 0, read);
                            crc.update(buf, 0, read);
                        }
                    }
                    outEntry.setSize(size);
                    outEntry.setCrc(crc.getValue());
                    outJar.flush();
                    outJar.closeEntry();
                }
                jar.closeEntry();
                entry = jar.getNextEntry();
            }
            InputStream pin = getClass().getResourceAsStream("resources/vainstall.properties");
            Properties prop = new Properties();
            try {
                prop.load(pin);
            } catch (IOException exc) {
            }
            if (language == null) language = "default";
            prop.setProperty("vainstall.destination.language", language);
            ZipEntry outEntry = new ZipEntry("com/memoire/vainstall/resources/vainstall.properties");
            CRC32 crc = new CRC32();
            outEntry.setCrc(crc.getValue());
            outEntry.setSize(prop.size());
            outJar.putNextEntry(outEntry);
            prop.store(outJar, VAGlobals.NAME + " " + VAGlobals.VERSION);
            outEntry.setCrc(crc.getValue());
            outJar.closeEntry();
            jar.close();
            outJar.close();
            in.close();
        } catch (IOException e) {
            String message = e.getLocalizedMessage();
            message += "\n" + VAGlobals.i18n("Setup_ErrorUninstallScripts");
            e.printStackTrace();
            exitOnError(new IOException(message));
        }
        return oldlog;
    }
