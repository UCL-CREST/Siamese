    private void patchJar(File file) {
        log("Patching " + file, null);
        try {
            final int MAX_ENTRY_SIZE = 1024 * 1024;
            byte[] classBytes = new byte[MAX_ENTRY_SIZE];
            File newFile = new File(file.getAbsolutePath() + ".NEW");
            CRC32 crc32 = new CRC32();
            JarFile oldJarFile = new JarFile(file);
            Manifest manifest = oldJarFile.getManifest();
            FileOutputStream outputStream = new FileOutputStream(newFile);
            JarOutputStream jarOutput = new JarOutputStream(outputStream, manifest);
            Enumeration entries = oldJarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry oldEntry = (JarEntry) entries.nextElement();
                JarEntry newEntry = (JarEntry) oldEntry.clone();
                System.out.print(".");
                if (oldEntry.getName().equals("META-INF/MANIFEST.MF")) {
                    continue;
                }
                InputStream in = oldJarFile.getInputStream(oldEntry);
                int totalLen = 0;
                int readLen = 0;
                do {
                    totalLen += readLen;
                    readLen = in.read(classBytes, totalLen, MAX_ENTRY_SIZE - totalLen);
                } while (readLen > 0);
                if (totalLen == MAX_ENTRY_SIZE) {
                    log("ERROR= patchJAR: MAX_ENTRY_SIZE too small - " + MAX_ENTRY_SIZE, null);
                    return;
                }
                if ((totalLen > 0) && oldEntry.getName().endsWith(".class")) {
                    int mainVersion = classBytes[6] * 256 + classBytes[7];
                    int minorVersion = classBytes[4] * 256 + classBytes[5];
                    long version = mainVersion * 65536 + minorVersion;
                    if (version > _maxAllowedVersion) {
                        log(" - " + oldEntry.getName(), null);
                        mainVersion = (int) _maxAllowedVersion / 65536;
                        minorVersion = (int) _maxAllowedVersion % 65536;
                        classBytes[4] = (byte) (minorVersion / 256);
                        classBytes[5] = (byte) (minorVersion % 256);
                        classBytes[6] = (byte) (mainVersion / 256);
                        classBytes[7] = (byte) (mainVersion % 256);
                        crc32.reset();
                        crc32.update(classBytes, 0, totalLen);
                        newEntry.setCrc(crc32.getValue());
                        newEntry.setCompressedSize(-1);
                    }
                }
                jarOutput.putNextEntry(newEntry);
                if (totalLen > 0) {
                    jarOutput.write(classBytes, 0, totalLen);
                }
                jarOutput.closeEntry();
            }
            jarOutput.close();
            outputStream.close();
            oldJarFile.close();
            log("", null);
            File bakFile = new File(file.getAbsolutePath() + ".BAK");
            if (!file.renameTo(bakFile)) {
                log("ERROR= Cannot rename " + file + " to " + bakFile + ". EXITING.", null);
                System.exit(99);
            }
            if (!newFile.renameTo(file)) {
                log("ERROR= Cannot rename " + newFile + " to " + file + ". EXITING.", null);
                System.exit(99);
            }
        } catch (Exception e) {
            log("patchJar", e);
        }
    }
