    private void backup(File file) {
        if (!createUninstaller) return;
        String relative = file.getAbsolutePath().substring(applicationDirectoryName.length());
        log.log("backup for " + file.getAbsolutePath());
        if (!file.exists()) {
            newEntries.add(relative);
        } else {
            try {
                FileInputStream in = new FileInputStream(file);
                ZipEntry ze = new ZipEntry(relative);
                ze.setSize(file.length());
                ze.setCompressedSize(-1);
                uninstallStream.putNextEntry(ze);
                int n;
                while (true) {
                    n = in.read(buf);
                    if (n <= 0) {
                        break;
                    }
                    uninstallStream.write(buf, 0, n);
                }
                in.close();
                uninstallStream.closeEntry();
                uninstallStream.flush();
            } catch (IOException e) {
                throw new UpdateIOException("Error creating backup entry for " + file.getAbsolutePath(), e);
            }
        }
    }
