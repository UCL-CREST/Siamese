    private void unpackBundle() throws IOException {
        File useJarPath = null;
        if (DownloadManager.isWindowsVista()) {
            useJarPath = lowJarPath;
            File jarDir = useJarPath.getParentFile();
            if (jarDir != null) {
                jarDir.mkdirs();
            }
        } else {
            useJarPath = jarPath;
        }
        DownloadManager.log("Unpacking " + this + " to " + useJarPath);
        InputStream rawStream = new FileInputStream(localPath);
        JarInputStream in = new JarInputStream(rawStream) {

            public void close() throws IOException {
            }
        };
        try {
            File jarTmp = null;
            JarEntry entry;
            while ((entry = in.getNextJarEntry()) != null) {
                String entryName = entry.getName();
                if (entryName.equals("classes.pack")) {
                    File packTmp = new File(useJarPath + ".pack");
                    packTmp.getParentFile().mkdirs();
                    DownloadManager.log("Writing temporary .pack file " + packTmp);
                    OutputStream tmpOut = new FileOutputStream(packTmp);
                    try {
                        DownloadManager.send(in, tmpOut);
                    } finally {
                        tmpOut.close();
                    }
                    jarTmp = new File(useJarPath + ".tmp");
                    DownloadManager.log("Writing temporary .jar file " + jarTmp);
                    unpack(packTmp, jarTmp);
                    packTmp.delete();
                } else if (!entryName.startsWith("META-INF")) {
                    File dest;
                    if (DownloadManager.isWindowsVista()) {
                        dest = new File(lowJavaPath, entryName.replace('/', File.separatorChar));
                    } else {
                        dest = new File(DownloadManager.JAVA_HOME, entryName.replace('/', File.separatorChar));
                    }
                    if (entryName.equals(BUNDLE_JAR_ENTRY_NAME)) dest = useJarPath;
                    File destTmp = new File(dest + ".tmp");
                    boolean exists = dest.exists();
                    if (!exists) {
                        DownloadManager.log(dest + ".mkdirs()");
                        dest.getParentFile().mkdirs();
                    }
                    try {
                        DownloadManager.log("Using temporary file " + destTmp);
                        FileOutputStream out = new FileOutputStream(destTmp);
                        try {
                            byte[] buffer = new byte[2048];
                            int c;
                            while ((c = in.read(buffer)) > 0) out.write(buffer, 0, c);
                        } finally {
                            out.close();
                        }
                        if (exists) dest.delete();
                        DownloadManager.log("Renaming from " + destTmp + " to " + dest);
                        if (!destTmp.renameTo(dest)) {
                            throw new IOException("unable to rename " + destTmp + " to " + dest);
                        }
                    } catch (IOException e) {
                        if (!exists) throw e;
                    }
                }
            }
            if (jarTmp != null) {
                if (useJarPath.exists()) jarTmp.delete(); else if (!jarTmp.renameTo(useJarPath)) {
                    throw new IOException("unable to rename " + jarTmp + " to " + useJarPath);
                }
            }
            if (DownloadManager.isWindowsVista()) {
                DownloadManager.log("Using broker to move " + name);
                if (!DownloadManager.moveDirWithBroker(DownloadManager.getKernelJREDir() + name)) {
                    throw new IOException("unable to create " + name);
                }
                DownloadManager.log("Broker finished " + name);
            }
            DownloadManager.log("Finished unpacking " + this);
        } finally {
            rawStream.close();
        }
        if (deleteOnInstall) {
            localPath.delete();
        }
    }
