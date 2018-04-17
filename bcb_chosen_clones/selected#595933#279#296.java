    private void copyEntries() {
        if (zipFile != null) {
            Enumeration<? extends ZipEntry> enumerator = zipFile.entries();
            while (enumerator.hasMoreElements()) {
                ZipEntry entry = enumerator.nextElement();
                if (!entry.isDirectory() && !toIgnore.contains(normalizePath(entry.getName()))) {
                    ZipEntry originalEntry = new ZipEntry(entry.getName());
                    try {
                        zipOutput.putNextEntry(originalEntry);
                        IOUtils.copy(getInputStream(entry.getName()), zipOutput);
                        zipOutput.closeEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
