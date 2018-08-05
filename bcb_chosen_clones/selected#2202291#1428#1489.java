    protected void zipFile(InputStream in, ZipOutputStream zOut, String vPath, long lastModified, File fromArchive, int mode, ZipExtraField[] extra) throws IOException {
        if (entries.contains(vPath)) {
            if (duplicate.equals("preserve")) {
                logWhenWriting(vPath + " already added, skipping", Project.MSG_INFO);
                return;
            } else if (duplicate.equals("fail")) {
                throw new BuildException("Duplicate file " + vPath + " was found and the duplicate " + "attribute is 'fail'.");
            } else {
                logWhenWriting("duplicate file " + vPath + " found, adding.", Project.MSG_VERBOSE);
            }
        } else {
            logWhenWriting("adding entry " + vPath, Project.MSG_VERBOSE);
        }
        entries.put(vPath, vPath);
        if (!skipWriting) {
            ZipEntry ze = new ZipEntry(vPath);
            ze.setTime(lastModified);
            ze.setMethod(doCompress ? ZipEntry.DEFLATED : ZipEntry.STORED);
            if (!zOut.isSeekable() && !doCompress) {
                long size = 0;
                CRC32 cal = new CRC32();
                if (!in.markSupported()) {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int count = 0;
                    do {
                        size += count;
                        cal.update(buffer, 0, count);
                        bos.write(buffer, 0, count);
                        count = in.read(buffer, 0, buffer.length);
                    } while (count != -1);
                    in = new ByteArrayInputStream(bos.toByteArray());
                } else {
                    in.mark(Integer.MAX_VALUE);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int count = 0;
                    do {
                        size += count;
                        cal.update(buffer, 0, count);
                        count = in.read(buffer, 0, buffer.length);
                    } while (count != -1);
                    in.reset();
                }
                ze.setSize(size);
                ze.setCrc(cal.getValue());
            }
            ze.setUnixMode(mode);
            zOut.putNextEntry(ze);
            if (extra != null) {
                ze.setExtraFields(extra);
            }
            byte[] buffer = new byte[BUFFER_SIZE];
            int count = 0;
            do {
                if (count != 0) {
                    zOut.write(buffer, 0, count);
                }
                count = in.read(buffer, 0, buffer.length);
            } while (count != -1);
        }
        addedFiles.addElement(vPath);
    }
