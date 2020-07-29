    private boolean verify(File file, long checksum, long precalcChecksum) {
        boolean ok = false;
        File missing = new File(file.getParentFile(), file.getName() + "-missing");
        if (!file.exists()) {
            ok = false;
        } else {
            if (precalcChecksum == 0) {
                CRC32 checker = new CRC32();
                byte[] buf = new byte[8192];
                BufferedInputStream in = null;
                try {
                    in = new BufferedInputStream(new FileInputStream(file));
                    int len;
                    while ((len = in.read(buf)) >= 0) {
                        checker.update(buf, 0, len);
                    }
                    if (checker.getValue() == checksum) {
                        ok = true;
                    } else {
                        ok = false;
                    }
                } catch (FileNotFoundException e) {
                    ok = false;
                } catch (IOException e) {
                    ok = false;
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            Logging.getErrorLog().reportException("Failed to close input stream", e);
                        }
                    }
                }
            } else {
                if (checksum == precalcChecksum) {
                    ok = true;
                }
            }
        }
        if (ok) {
            calculatedChecksums.put(file.getName(), checksum);
            if (missing.exists()) {
                missing.delete();
            }
            return true;
        } else {
            try {
                if (file.exists()) {
                    boolean delok = file.delete();
                }
                if (!missing.exists()) {
                    missing.createNewFile();
                }
            } catch (IOException e1) {
                Logging.getErrorLog().reportError("Failed to create file: " + missing.getAbsolutePath());
            }
            return false;
        }
    }
