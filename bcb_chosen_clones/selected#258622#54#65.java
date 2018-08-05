    private void writeClassFile(File zipFile, ZipOutputStream out, JAssembler asm) {
        ClassDef def = asm.def;
        Box box = asm.classFile;
        String path = def.ns.replace('.', '/') + '/' + def.name + ".class";
        try {
            out.putNextEntry(new ZipEntry(path));
            out.write(box.buf, 0, box.len);
            out.closeEntry();
        } catch (IOException e) {
            throw err("Cannot write zip entry " + path, zipFile);
        }
    }
