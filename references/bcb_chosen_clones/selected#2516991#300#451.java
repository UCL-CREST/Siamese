        private boolean createMyMLEApps() throws Exception {
            boolean oswindows = (File.separatorChar == '\\');
            File dest = new File(export.getText());
            if (listModel.size() <= 0 || !dest.exists()) return false;
            File tempFolder = HelperOp.createTempDirectory();
            Enumeration en = listModel.elements();
            StringBuffer mloIds = new StringBuffer();
            StringBuffer xmlInstall = new StringBuffer();
            int count = 0;
            while (en.hasMoreElements()) {
                File f = new File((String) en.nextElement());
                if (f.exists()) {
                    ContentPackage lernobjekt = new ContentPackage();
                    HelperContentPackage.retrieveLernobjekt(lernobjekt, HelperOp.getLocalFile(f.getAbsolutePath()));
                    String xml = lernobjekt.getXml();
                    java.util.ArrayList vocTrainerIds = new java.util.ArrayList();
                    Pattern p = Pattern.compile("<voc[^>]*>");
                    Matcher m = p.matcher(xml);
                    while (m.find()) {
                        String voc = m.group(0);
                        Matcher m2 = Pattern.compile(" u[^=]*=\"([^\"]*)\"").matcher(voc);
                        if (!m2.find()) continue;
                        String id = m2.group(1);
                        System.out.println("Voc-Trainer found:" + id);
                        if (!HelperStd.isEmpty(id)) vocTrainerIds.add(id);
                    }
                    count++;
                    conf.parser.parseXMLPageSuite(xml, lernobjekt, false);
                    int binarycount = 0;
                    HashMap binariesReplace = new HashMap();
                    HashMap vocTrainers = new HashMap();
                    java.util.Iterator it = lernobjekt.getBinariesIDIterator();
                    while (it.hasNext()) {
                        binarycount++;
                        String id = (String) it.next();
                        File binary = new File(tempFolder.getAbsolutePath() + File.separatorChar + "mlo" + count + "_b" + binarycount);
                        if (!vocTrainerIds.contains(id)) {
                            HelperOp.saveFile(binary, lernobjekt.getBinary(id));
                        } else vocTrainers.put(id, binary);
                        xml = xml.replace("=\"" + id + "\"", "=\"" + "/mlo" + count + "_b" + binarycount + "\"");
                        binariesReplace.put(id, "/mlo" + count + "_b" + binarycount);
                    }
                    for (int v = 0; v < vocTrainerIds.size(); v++) {
                        String id = (String) vocTrainerIds.get(v);
                        File binary = (File) vocTrainers.get(id);
                        if (binary == null) continue;
                        String text = new String(lernobjekt.getBinary(id));
                        java.util.Iterator itbin = binariesReplace.keySet().iterator();
                        while (itbin.hasNext()) {
                            String idbin = (String) itbin.next();
                            text = text.replace("=\"" + idbin + "\"", "=\"" + binariesReplace.get(idbin) + "\"");
                        }
                        HelperOp.saveFile(binary, text.getBytes());
                    }
                    HelperOp.saveFile(new File(tempFolder.getAbsolutePath() + File.separatorChar + "mlo" + count + "_index.xml"), xml.getBytes());
                    xmlInstall.append("<button t=\"d\" action=\"/mlo" + count + "_index.xml\" data=\"l\" w=\"" + HelperXMLParserSimple.replaceXMLChars(lernobjekt.getTitel()) + "\" f=\"1\">\n");
                    if (mloIds.length() > 0) mloIds.append("|");
                    mloIds.append("/mlo" + count + "_index.xml");
                    lernobjekt = null;
                }
            }
            if (oswindows) Thread.sleep(500);
            boolean blackberrysupport = (new File(conf.appParams.getParameter("installpath") + File.separatorChar + "rapc.jar")).exists() && (new File(conf.appParams.getParameter("installpath") + File.separatorChar + "net_rim_api.jar").exists());
            System.out.println("BlackBerry support: " + blackberrysupport);
            String[] devices = conf.appParams.getParameter("mymle.devices", "").split(",");
            for (int d = 0; d < devices.length; d++) {
                File path = new File(conf.appParams.getParameter("mymle." + devices[d] + ".path", null));
                if (!path.exists()) continue;
                String[] files = conf.appParams.getParameter("mymle." + devices[d] + ".filenames", "").split(",");
                for (int f = 0; f < files.length; f++) {
                    File jar = new File(path.getAbsolutePath() + File.separatorChar + files[f]);
                    File jardestpath = new File(dest.getAbsolutePath() + File.separatorChar + devices[d]);
                    File jardest = new File(jardestpath.getAbsolutePath() + File.separatorChar + files[f]);
                    if (!jar.exists()) continue;
                    jardestpath.mkdirs();
                    jardest.createNewFile();
                    System.out.println("Reading: " + jar.getAbsolutePath());
                    System.out.println("Writing: " + jardest.getAbsolutePath());
                    ZipInputStream zin1 = new ZipInputStream(new FileInputStream(jar));
                    ZipOutputStream out1 = new ZipOutputStream(new FileOutputStream(jardest));
                    ZipEntry entry1 = zin1.getNextEntry();
                    byte[] buf = new byte[1024];
                    while (entry1 != null) {
                        String name = entry1.getName();
                        out1.putNextEntry(new java.util.zip.ZipEntry(name));
                        if (name.compareTo("inst.xml") == 0) {
                            int len;
                            ByteArrayOutputStream bout = new ByteArrayOutputStream();
                            while ((len = zin1.read(buf)) > 0) {
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
                            out1.write(b);
                        } else {
                            int len;
                            while ((len = zin1.read(buf)) > 0) {
                                out1.write(buf, 0, len);
                            }
                        }
                        entry1 = zin1.getNextEntry();
                    }
                    zin1.close();
                    System.out.println("Adding MLOs: " + tempFolder.getAbsolutePath());
                    addRecursivly(out1, tempFolder, tempFolder.getAbsolutePath());
                    out1.close();
                    if (files[f].indexOf(".jar") > 0) {
                        File jad1 = new File(path.getAbsolutePath() + File.separatorChar + files[f].replace(".jar", ".jad"));
                        File jad2 = new File(path.getAbsolutePath() + File.separatorChar + "ota_" + files[f].replace(".jar", ".jad"));
                        System.out.println("Processing JAD: " + jad1.getAbsolutePath());
                        if (jad1.exists()) {
                            String jad = new String(HelperOp.getLocalFile(jad1.getAbsolutePath()));
                            jad = jad.replace(jar.length() + "", jardest.length() + "");
                            File jad1dest = new File(jardestpath.getAbsolutePath() + File.separatorChar + files[f].replace(".jar", ".jad"));
                            HelperOp.saveFile(jad1dest, jad.getBytes());
                        }
                        if (jad2.exists()) {
                            String jad = new String(HelperOp.getLocalFile(jad2.getAbsolutePath()));
                            jad = jad.replace(jar.length() + "", jardest.length() + "");
                            File jad2dest = new File(jardestpath.getAbsolutePath() + File.separatorChar + "ota_" + files[f].replace(".jar", ".jad"));
                            HelperOp.saveFile(jad2dest, jad.getBytes());
                        }
                    }
                    if (blackberrysupport && conf.appParams.getParameter("mymle." + devices[d] + ".blackberry", "false").compareTo("true") == 0) {
                        executeProcess((oswindows ? conf.appParams.getParameter("cmd.blackberry.rapc.windows") : conf.appParams.getParameter("cmd.blackberry.rapc.linux")).replace("{0}", processFilePathForOS(jardest.getAbsolutePath().replace(".jar", ""), oswindows)).replace("{1}", processFilePathForOS(path.getAbsolutePath() + File.separatorChar + files[f].replace(".jar", ".jad"), oswindows)).replace("{2}", processFilePathForOS(jardest.getAbsolutePath(), oswindows)), new File(conf.appParams.getParameter("installpath")), true);
                        File jad1 = new File(path.getAbsolutePath() + File.separatorChar + "cod" + File.separatorChar + files[f].replace(".jar", ".alx"));
                        if (!jad1.exists()) jad1 = new File(path.getAbsolutePath() + File.separatorChar + files[f].replace(".jar", ".alx"));
                        if (jad1.exists()) {
                            String jad = new String(HelperOp.getLocalFile(jad1.getAbsolutePath()));
                            jad = jad.replace(jar.length() + "", jardest.length() + "");
                            File jad1dest = new File(jardestpath.getAbsolutePath() + File.separatorChar + files[f].replace(".jar", ".alx"));
                            HelperOp.saveFile(jad1dest, jad.getBytes());
                        }
                    }
                    System.out.println("finished: " + devices[d] + ":" + files[f]);
                }
            }
            processAndroidPackages(dest, mloIds, xmlInstall, tempFolder);
            try {
                HelperOp.saveFile(new File(dest.getAbsolutePath() + File.separatorChar + "README-LIESMICH.txt"), HelperOp.getLocalFile(conf.appParams.getParameter("installpath") + File.separatorChar + "README.txt"));
            } catch (Exception eee) {
                eee.printStackTrace();
            }
            HelperOp.deleteDir(tempFolder);
            return true;
        }
