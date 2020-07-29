        private void writeConstData() throws IOException {
            zos.putNextEntry(new ZipEntry("const.bin"));
            outFile = new DataOutputStream(zos);
            buf.position(0);
            outFile.writeDouble(-1.);
            quad.writeConstData(buf);
            outFile.writeInt(buf.position());
            outFile.write(buf.array(), 0, buf.position());
            zos.closeEntry();
        }
