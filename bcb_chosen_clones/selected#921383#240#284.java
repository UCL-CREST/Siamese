    protected void removeEntryJD(ZipException ze, File src, String backUpPath, FileOutputStream fileoutputstream1) throws IOException {
        Enumeration enumer;
        String line;
        try {
            File newZip = new File(tempDir, src.getName() + System.currentTimeMillis() + PatchRandom.getRandom());
            JarOutputStream jout = new JarOutputStream(new FileOutputStream(newZip.getAbsoluteFile()));
            ZipFile oldZip = new ZipFile(src.getAbsolutePath());
            enumer = oldZip.entries();
            while (enumer.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enumer.nextElement();
                InputStream is = oldZip.getInputStream(zipEntry);
                if (zipEntry.getName().equals("META-INF/INDEX.JD")) {
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    ZipEntry newZipEntry = new ZipEntry(zipEntry.getName());
                    jout.putNextEntry(newZipEntry);
                    while ((line = br.readLine()) != null) {
                        if (line.indexOf(ze.getMessage().substring("duplicate entry:".length())) > -1) continue;
                        if (line.startsWith("move")) {
                            List list1 = getSubpaths(line.substring("move".length()));
                            if (hasEntry(oldZip, (String) list1.get(1)) && !((String) list1.get(1)).equals("META-INF")) {
                                continue;
                            }
                        }
                        jout.write(line.getBytes());
                        jout.write(Character.LINE_SEPARATOR);
                    }
                    jout.closeEntry();
                    isr.close();
                    br.close();
                } else {
                    jout.putNextEntry(zipEntry);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) != -1) jout.write(buffer, 0, len);
                    jout.closeEntry();
                    is.close();
                }
            }
            jout.close();
            new JarDiffPatcher().applyPatch(null, backUpPath, newZip.getAbsolutePath(), fileoutputstream1);
        } catch (IOException e2) {
            throw e2;
        }
    }
