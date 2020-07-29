    private boolean writeUninstallData() {
        try {
            UninstallData udata = UninstallData.getInstance();
            List files = udata.getUninstalableFilesList();
            ZipOutputStream outJar = this.idata.uninstallOutJar;
            if (outJar == null) {
                return true;
            }
            System.out.println("[ Writing the uninstaller data ... ]");
            outJar.putNextEntry(new ZipEntry("install.log"));
            BufferedWriter logWriter = new BufferedWriter(new OutputStreamWriter(outJar));
            logWriter.write(this.idata.getInstallPath());
            logWriter.newLine();
            Iterator iter = files.iterator();
            while (iter.hasNext()) {
                logWriter.write((String) iter.next());
                if (iter.hasNext()) {
                    logWriter.newLine();
                }
            }
            logWriter.flush();
            outJar.closeEntry();
            outJar.putNextEntry(new ZipEntry("jarlocation.log"));
            logWriter = new BufferedWriter(new OutputStreamWriter(outJar));
            logWriter.write(udata.getUninstallerJarFilename());
            logWriter.newLine();
            logWriter.write(udata.getUninstallerPath());
            logWriter.flush();
            outJar.closeEntry();
            outJar.putNextEntry(new ZipEntry("executables"));
            ObjectOutputStream execStream = new ObjectOutputStream(outJar);
            iter = udata.getExecutablesList().iterator();
            execStream.writeInt(udata.getExecutablesList().size());
            while (iter.hasNext()) {
                ExecutableFile file = (ExecutableFile) iter.next();
                execStream.writeObject(file);
            }
            execStream.flush();
            outJar.closeEntry();
            Map<String, Object> additionalData = udata.getAdditionalData();
            if (additionalData != null && !additionalData.isEmpty()) {
                Iterator<String> keys = additionalData.keySet().iterator();
                HashSet<String> exist = new HashSet<String>();
                while (keys != null && keys.hasNext()) {
                    String key = keys.next();
                    Object contents = additionalData.get(key);
                    if ("__uninstallLibs__".equals(key)) {
                        Iterator nativeLibIter = ((List) contents).iterator();
                        while (nativeLibIter != null && nativeLibIter.hasNext()) {
                            String nativeLibName = (String) ((List) nativeLibIter.next()).get(0);
                            byte[] buffer = new byte[5120];
                            long bytesCopied = 0;
                            int bytesInBuffer;
                            outJar.putNextEntry(new ZipEntry("native/" + nativeLibName));
                            InputStream in = getClass().getResourceAsStream("/native/" + nativeLibName);
                            while ((bytesInBuffer = in.read(buffer)) != -1) {
                                outJar.write(buffer, 0, bytesInBuffer);
                                bytesCopied += bytesInBuffer;
                            }
                            outJar.closeEntry();
                        }
                    } else if ("uninstallerListeners".equals(key) || "uninstallerJars".equals(key)) {
                        ArrayList<String> subContents = new ArrayList<String>();
                        Iterator listenerIter = ((List) contents).iterator();
                        while (listenerIter.hasNext()) {
                            byte[] buffer = new byte[5120];
                            long bytesCopied = 0;
                            int bytesInBuffer;
                            CustomData customData = (CustomData) listenerIter.next();
                            if (customData.listenerName != null) {
                                subContents.add(customData.listenerName);
                            }
                            Iterator<String> liClaIter = customData.contents.iterator();
                            while (liClaIter.hasNext()) {
                                String contentPath = liClaIter.next();
                                if (exist.contains(contentPath)) {
                                    continue;
                                }
                                exist.add(contentPath);
                                try {
                                    outJar.putNextEntry(new ZipEntry(contentPath));
                                } catch (ZipException ze) {
                                    Debug.trace("ZipException in writing custom data: " + ze.getMessage());
                                    continue;
                                }
                                InputStream in = getClass().getResourceAsStream("/" + contentPath);
                                if (in != null) {
                                    while ((bytesInBuffer = in.read(buffer)) != -1) {
                                        outJar.write(buffer, 0, bytesInBuffer);
                                        bytesCopied += bytesInBuffer;
                                    }
                                } else {
                                    Debug.trace("custom data not found: " + contentPath);
                                }
                                outJar.closeEntry();
                            }
                        }
                        outJar.putNextEntry(new ZipEntry(key));
                        ObjectOutputStream objOut = new ObjectOutputStream(outJar);
                        objOut.writeObject(subContents);
                        objOut.flush();
                        outJar.closeEntry();
                    } else {
                        outJar.putNextEntry(new ZipEntry(key));
                        if (contents instanceof ByteArrayOutputStream) {
                            ((ByteArrayOutputStream) contents).writeTo(outJar);
                        } else {
                            ObjectOutputStream objOut = new ObjectOutputStream(outJar);
                            objOut.writeObject(contents);
                            objOut.flush();
                        }
                        outJar.closeEntry();
                    }
                }
            }
            ArrayList<String> unInstallScripts = udata.getUninstallScripts();
            Iterator<String> unInstallIter = unInstallScripts.iterator();
            ObjectOutputStream rootStream;
            int idx = 0;
            while (unInstallIter.hasNext()) {
                outJar.putNextEntry(new ZipEntry(UninstallData.ROOTSCRIPT + Integer.toString(idx)));
                rootStream = new ObjectOutputStream(outJar);
                String unInstallScript = (String) unInstallIter.next();
                rootStream.writeUTF(unInstallScript);
                rootStream.flush();
                outJar.closeEntry();
            }
            outJar.flush();
            outJar.close();
            return true;
        } catch (Exception err) {
            err.printStackTrace();
            return false;
        }
    }
