    protected void processJarFile(File file) throws Exception {
        if (verbose) {
            log("processing " + file.toURL());
        }
        File tempFile = File.createTempFile(file.getName(), null, new File(file.getAbsoluteFile().getParent()));
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(file));
            try {
                FileOutputStream fout = new FileOutputStream(tempFile, false);
                try {
                    ZipOutputStream out = new ZipOutputStream(fout);
                    ZipEntry entry;
                    while ((entry = zip.getNextEntry()) != null) {
                        byte bytes[] = getBytes(zip);
                        if (!entry.isDirectory()) {
                            DataInputStream din = new DataInputStream(new ByteArrayInputStream(bytes));
                            if (din.readInt() == CLASS_MAGIC) {
                                bytes = process(bytes);
                            } else {
                                if (verbose) {
                                    log("ignoring " + entry.toString());
                                }
                            }
                        }
                        ZipEntry outEntry = new ZipEntry(entry.getName());
                        outEntry.setMethod(entry.getMethod());
                        outEntry.setComment(entry.getComment());
                        outEntry.setSize(bytes.length);
                        if (outEntry.getMethod() == ZipEntry.STORED) {
                            CRC32 crc = new CRC32();
                            crc.update(bytes);
                            outEntry.setCrc(crc.getValue());
                            outEntry.setCompressedSize(bytes.length);
                        }
                        out.putNextEntry(outEntry);
                        out.write(bytes);
                        out.closeEntry();
                        zip.closeEntry();
                    }
                    out.close();
                } finally {
                    fout.close();
                }
            } finally {
                zip.close();
            }
            if (file.delete()) {
                File newFile = new File(tempFile.getAbsolutePath());
                if (!newFile.renameTo(file)) {
                    throw new IOException("can not rename " + tempFile + " to " + file);
                }
            } else {
                throw new IOException("can not delete " + file);
            }
        } finally {
            tempFile.delete();
        }
    }
