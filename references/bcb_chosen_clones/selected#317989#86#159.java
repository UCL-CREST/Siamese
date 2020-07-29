    public void execute() throws BuildException {
        boolean manifestFound = false;
        ZipFile zipFile = null;
        ZipInputStream zipInputStream = null;
        ZipOutputStream zipFileOutputStream = null;
        FileInputStream fileInputStream = null;
        File fileIn = new File(m_jarPath);
        if (!fileIn.exists()) {
            throw new BuildException("File " + m_jarPath + " does not exists.");
        }
        File fileOut = new File(m_jarPath + ".new");
        try {
            zipFile = new ZipFile(fileIn);
            fileInputStream = new FileInputStream(fileIn);
            zipInputStream = new ZipInputStream(fileInputStream);
            FileOutputStream fileOutputStream = new FileOutputStream(fileOut);
            zipFileOutputStream = new ZipOutputStream(fileOutputStream);
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                if (entry.isDirectory()) {
                } else {
                    InputStream in = zipFile.getInputStream(entry);
                    byte[] content = readInputStream(in);
                    if (entry.getName().endsWith("manifest.mf")) {
                        manifestFound = true;
                        String contenu = incrementVersionInManifest(content);
                        zipFileOutputStream.putNextEntry(entry);
                        zipFileOutputStream.write(contenu.getBytes());
                        zipFileOutputStream.flush();
                    } else {
                        zipFileOutputStream.putNextEntry(entry);
                        zipFileOutputStream.write(content);
                        zipFileOutputStream.flush();
                    }
                }
                entry = zipInputStream.getNextEntry();
            }
            if (!manifestFound) {
                ZipEntry newEntry = new ZipEntry("/meta-inf/manifest.mf");
                zipFileOutputStream.putNextEntry(newEntry);
                if (m_newMajorRelease == null) {
                    m_newMajorRelease = "1.0";
                }
                if (m_newMinorRelease == null) {
                    m_newMinorRelease = "1";
                }
                String content = "Manifest-Version: " + m_newMajorRelease + "\nImplementation-Version: \"Build (" + m_newMinorRelease + ")\"";
                if ((m_envType != null) && (m_envType.length() > 0)) {
                    content += (ENVTYPE_HEADER_STRING + m_envType + "\n");
                }
                zipFileOutputStream.write(content.getBytes());
                zipFileOutputStream.flush();
                m_newVersion = m_newMajorRelease + "." + m_newMinorRelease;
            }
        } catch (Exception e) {
            throw new BuildException(e.getMessage(), e);
        } finally {
            try {
                if (zipFileOutputStream != null) {
                    zipFileOutputStream.close();
                }
                if (zipInputStream != null) {
                    zipInputStream.close();
                }
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException e) {
            }
        }
        fileIn.delete();
        fileOut.renameTo(fileIn);
        System.out.println("Version increased in jar " + m_jarPath + " to " + m_newVersion);
    }
