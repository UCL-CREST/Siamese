        private void processAndroidPackages(File dest, StringBuffer mloIds, StringBuffer xmlInstall, File tempFolder) throws Exception {
            File androidzip = new File(conf.appParams.getParameter("installpath") + File.separatorChar + "android.zip");
            if (!androidzip.exists()) {
                System.out.println("No Android Support! Android.zip is missing!");
                return;
            }
            boolean oswindows = (File.separatorChar == '\\');
            System.out.println("processAndroidPackages:" + oswindows);
            String[] devices = conf.appParams.getParameter("mymle.android.devices", "").split(",");
            for (int d = 0; d < devices.length; d++) {
                if (devices[d].trim().length() <= 0) continue;
                File path = new File(conf.appParams.getParameter("mymle." + devices[d] + ".path", null));
                if (!path.exists()) continue;
                String[] files = conf.appParams.getParameter("mymle." + devices[d] + ".filenames", "").split(",");
                for (int f = 0; f < files.length; f++) {
                    File jar = new File(path.getAbsolutePath() + File.separatorChar + files[f]);
                    File jardestpath = new File(dest.getAbsolutePath() + File.separatorChar + devices[d]);
                    File jardest = new File(jardestpath.getAbsolutePath() + File.separatorChar + files[f]);
                    System.out.println("apk:" + jar);
                    if (!jar.exists()) continue;
                    jardestpath.mkdirs();
                    jardest.createNewFile();
                    File tempFolderAndroid = HelperOp.createTempDirectory();
                    ZipInputStream zinandroid1 = new ZipInputStream(new FileInputStream(androidzip));
                    System.out.println("android.zip:" + conf.appParams.getParameter("installpath") + File.separatorChar + "android.zip");
                    ZipEntry entry1 = zinandroid1.getNextEntry();
                    byte[] buf = new byte[1024];
                    while (entry1 != null) {
                        String name = entry1.getName();
                        int len;
                        File destAnd = new File(tempFolderAndroid.getAbsolutePath() + File.separatorChar + name);
                        if (entry1.isDirectory()) {
                            destAnd.mkdirs();
                            entry1 = zinandroid1.getNextEntry();
                            continue;
                        }
                        destAnd.getParentFile().mkdirs();
                        destAnd.createNewFile();
                        FileOutputStream bout = new FileOutputStream(destAnd);
                        while ((len = zinandroid1.read(buf)) > 0) {
                            bout.write(buf, 0, len);
                        }
                        bout.close();
                        entry1 = zinandroid1.getNextEntry();
                    }
                    zinandroid1.close();
                    File resraw = new File(tempFolderAndroid.getAbsolutePath() + File.separatorChar + "res" + File.separatorChar + "raw");
                    System.out.println("res/raw/:" + resraw);
                    resraw.mkdirs();
                    ZipInputStream zin1 = new ZipInputStream(new FileInputStream(jar));
                    ZipEntry entry2 = zin1.getNextEntry();
                    while (entry2 != null) {
                        String name = entry2.getName();
                        if (name.indexOf("res/raw/") >= 0) {
                            int len;
                            File destAnd = new File(tempFolderAndroid.getAbsolutePath() + File.separatorChar + name);
                            destAnd.createNewFile();
                            FileOutputStream bout = new FileOutputStream(destAnd);
                            while ((len = zin1.read(buf)) > 0) {
                                bout.write(buf, 0, len);
                            }
                            bout.close();
                        }
                        entry2 = zin1.getNextEntry();
                    }
                    zin1.close();
                    System.out.println("addRecursivlyFiles:" + tempFolder);
                    if (oswindows) Thread.sleep(150);
                    addRecursivlyFiles(resraw, tempFolder, tempFolder.getAbsolutePath());
                    if (!oswindows) executeProcess("chmod +x aapt", tempFolderAndroid, false);
                    if (oswindows) Thread.sleep(500);
                    executeProcess((oswindows ? conf.appParams.getParameter("cmd.android.aapk.windows") : conf.appParams.getParameter("cmd.android.aapk.linux")).replace("{0}", conf.appParams.getParameter("mymle." + devices[d] + ".version", "15")), tempFolderAndroid, false);
                    if (oswindows) Thread.sleep(500);
                    ZipInputStream zinandroid2 = new ZipInputStream(new FileInputStream(new File(tempFolderAndroid.getAbsolutePath() + File.separatorChar + "test.apk")));
                    System.out.println("test.apk:" + tempFolderAndroid.getAbsolutePath() + File.separatorChar + "test.apk");
                    ZipEntry entry3 = zinandroid2.getNextEntry();
                    while (entry3 != null) {
                        String name = entry3.getName();
                        if (name.compareTo("resources.arsc") == 0) {
                            File dres = new File(tempFolderAndroid.getAbsolutePath() + File.separatorChar + name);
                            dres.createNewFile();
                            int len;
                            FileOutputStream bout = new FileOutputStream(dres);
                            while ((len = zinandroid2.read(buf)) > 0) {
                                bout.write(buf, 0, len);
                            }
                            bout.close();
                            break;
                        }
                        entry3 = zinandroid2.getNextEntry();
                    }
                    zinandroid2.close();
                    System.out.println("package apk:" + jar);
                    System.out.println("package apk dest:" + jardest);
                    ZipInputStream zin2 = new ZipInputStream(new FileInputStream(jar));
                    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(jardest));
                    ZipEntry entry4 = zin2.getNextEntry();
                    while (entry4 != null) {
                        String name = entry4.getName();
                        if (name.indexOf("res/raw/") >= 0) {
                            entry4 = zin2.getNextEntry();
                            continue;
                        }
                        if (name.indexOf("META-INF/") >= 0) {
                            if (name.indexOf("MANIFEST") >= 0) {
                                out.putNextEntry(new java.util.zip.ZipEntry(name));
                                out.write("Manifest-Version: 1.0\nCreated-By: 1.0 (Android)\n".getBytes());
                            }
                            entry4 = zin2.getNextEntry();
                            continue;
                        }
                        out.putNextEntry(new java.util.zip.ZipEntry(name));
                        int len;
                        if (name.compareTo("resources.arsc") == 0) {
                            FileInputStream fin = new FileInputStream(tempFolderAndroid.getAbsolutePath() + File.separatorChar + "resources.arsc");
                            while ((len = fin.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }
                            fin.close();
                        } else {
                            while ((len = zin2.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }
                        }
                        entry4 = zin2.getNextEntry();
                    }
                    zin2.close();
                    System.out.println("adding MLOs to apk from:" + resraw);
                    String[] children = resraw.list();
                    for (int i = 0; i < children.length; i++) {
                        File file = new File(resraw, children[i]);
                        if (file.isDirectory()) continue;
                        String filename = file.getAbsolutePath().replace(tempFolderAndroid.getAbsolutePath(), "");
                        if (filename.indexOf("/") == 0) filename = filename.substring(1);
                        if (filename.indexOf(File.separatorChar) == 0) filename = filename.substring(1);
                        filename = filename.replace("\\", "/");
                        out.putNextEntry(new java.util.zip.ZipEntry(filename));
                        int len;
                        FileInputStream fin = new FileInputStream(file);
                        if (filename.indexOf("res/raw/inst.xml") >= 0) {
                            ByteArrayOutputStream bout = new ByteArrayOutputStream();
                            while ((len = fin.read(buf)) > 0) {
                                bout.write(buf, 0, len);
                            }
                            String installxml = new String(bout.toByteArray());
                            installxml = installxml.replace("{0}", "<button t=\"dm\" action=\"" + mloIds.toString() + "\" data=\"l\" w=\"--All--\" f=\"1\">\n" + xmlInstall);
                            byte[] b = null;
                            try {
                                b = installxml.getBytes("UTF-8");
                            } catch (Exception eee) {
                                b = installxml.getBytes();
                            }
                            out.write(b);
                        } else {
                            while ((len = fin.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }
                        }
                        fin.close();
                    }
                    out.close();
                    executeProcess((oswindows ? conf.appParams.getParameter("cmd.android.signing.windows") : conf.appParams.getParameter("cmd.android.signing.linux")).replace("{0}", processFilePathForOS(jardest.getAbsolutePath(), oswindows)), tempFolderAndroid, false);
                    HelperOp.deleteDir(tempFolderAndroid);
                }
            }
        }
