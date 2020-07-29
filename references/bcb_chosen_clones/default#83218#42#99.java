    public void update(boolean compressManifest) throws BuildException {
        try {
            FileInputStream fis = new FileInputStream(this.file);
            ZipInputStream zis = new ZipInputStream(fis);
            File temp = File.createTempFile("jst", ".jar", this.file.getParentFile());
            FileOutputStream fos = new FileOutputStream(temp);
            ZipOutputStream zos = new ZipOutputStream(fos);
            zos.setLevel(Deflater.BEST_COMPRESSION);
            ZipEntry ze, newZe;
            CRC32 checksum = new CRC32();
            byte[] buf = new byte[1024];
            while ((ze = zis.getNextEntry()) != null) {
                if (ze.getName().equals("META-INF/MANIFEST.MF")) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(zis));
                    StringBuffer sb = new StringBuffer();
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.indexOf("MIDlet-Jar-Size") == -1) {
                            sb.append(line);
                        } else {
                            sb.append("MIDlet-Jar-Size: " + this.file.length());
                        }
                        sb.append('\n');
                    }
                    zis.closeEntry();
                    byte[] raw = sb.toString().getBytes();
                    newZe = new ZipEntry(ze);
                    checksum.update(raw);
                    ze.setCrc(checksum.getValue());
                    ze.setSize(raw.length);
                    ze.setMethod(compressManifest ? Deflater.DEFLATED : Deflater.NO_COMPRESSION);
                    ze.setCompressedSize(-1);
                    zos.putNextEntry(ze);
                    zos.write(raw, 0, raw.length);
                    zos.closeEntry();
                } else {
                    newZe = new ZipEntry(ze);
                    newZe.setCompressedSize(-1);
                    zos.putNextEntry(newZe);
                    while (zis.available() != 0) {
                        int len = zis.read(buf, 0, 1024);
                        if (len != -1) {
                            zos.write(buf, 0, len);
                        }
                    }
                    zos.closeEntry();
                    zis.closeEntry();
                }
            }
            zis.close();
            zos.close();
            if (!this.file.delete() || !temp.renameTo(this.file)) {
                throw (new BuildException("Could not delete or create " + file));
            }
        } catch (IOException e) {
            throw (new BuildException(e.getMessage()));
        }
    }
