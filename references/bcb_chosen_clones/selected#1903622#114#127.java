    private void writeDynData(final int time_s) {
        try {
            this.zos.putNextEntry(new ZipEntry("step." + time_s + ".bin"));
            DataOutputStream outFile = new DataOutputStream(this.zos);
            this.buf.position(0);
            outFile.writeDouble(time_s);
            this.quad.writeDynData(null, this.buf);
            outFile.writeInt(this.buf.position());
            outFile.write(this.buf.array(), 0, this.buf.position());
            this.zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
