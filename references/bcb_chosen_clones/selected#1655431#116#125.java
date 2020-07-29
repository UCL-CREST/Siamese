        private void writeConstData() throws IOException {
            this.zos.putNextEntry(new ZipEntry("const.bin"));
            this.outFile = new DataOutputStream(this.zos);
            this.buf.position(0);
            this.outFile.writeDouble(-1.);
            this.quad.writeConstData(this.buf);
            this.outFile.writeInt(this.buf.position());
            this.outFile.write(this.buf.array(), 0, this.buf.position());
            this.zos.closeEntry();
        }
