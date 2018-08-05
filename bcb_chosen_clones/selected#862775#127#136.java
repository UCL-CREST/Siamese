        private void writeDynData(int time_s) throws IOException {
            this.zos.putNextEntry(new ZipEntry("step." + time_s + ".bin"));
            this.outFile = new DataOutputStream(this.zos);
            this.buf.position(0);
            this.outFile.writeDouble(time_s);
            this.quad.writeDynData(null, this.buf);
            this.outFile.writeInt(this.buf.position());
            this.outFile.write(this.buf.array(), 0, this.buf.position());
            this.zos.closeEntry();
        }
