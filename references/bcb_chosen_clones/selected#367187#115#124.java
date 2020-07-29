        private void writeDynData(int time_s) throws IOException {
            zos.putNextEntry(new ZipEntry("step." + time_s + ".bin"));
            outFile = new DataOutputStream(zos);
            buf.position(0);
            outFile.writeDouble(time_s);
            quad.writeDynData(null, buf);
            outFile.writeInt(buf.position());
            outFile.write(buf.array(), 0, buf.position());
            zos.closeEntry();
        }
