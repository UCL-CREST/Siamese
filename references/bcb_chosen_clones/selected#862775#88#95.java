        private void writeInfos() throws IOException {
            this.zos.putNextEntry(new ZipEntry("info.bin"));
            this.outFile = new DataOutputStream(this.zos);
            this.outFile.writeInt(VERSION);
            this.outFile.writeInt(MINORVERSION);
            this.outFile.writeDouble(this.intervall_s);
            this.zos.closeEntry();
        }
