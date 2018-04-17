    public void action(Map<String, ClassInfo> map, String... files) throws Exception {
        Remapper remapper = new Remapper(map);
        for (String file : files) {
            String newFile = file + ".br.jar";
            ZipFile zip = new ZipFile(file);
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(newFile)));
            for (Enumeration<? extends ZipEntry> entries = zip.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    zos.putNextEntry(new ZipEntry(entry.getName()));
                    zos.closeEntry();
                } else if (entry.getName().endsWith(".class")) {
                    ClassWriter writer = new ClassWriter(0);
                    ClassNameVisitor classNameVisitor = new ClassNameVisitor(writer);
                    ClassVisitor visitor = new ClassForNameFixVisitor(new RemappingClassAdapter(classNameVisitor, remapper), remapper);
                    new ClassReader(zip.getInputStream(entry)).accept(visitor, 0);
                    String className = classNameVisitor.getName();
                    ZipEntry newEntry = new ZipEntry(className + ".class");
                    zos.putNextEntry(newEntry);
                    zos.write(writer.toByteArray());
                    zos.closeEntry();
                } else {
                    zos.putNextEntry(new ZipEntry(entry.getName()));
                    byte[] buff = new byte[10240];
                    int cnt = 0;
                    InputStream is = zip.getInputStream(entry);
                    while ((cnt = is.read(buff)) > 0) {
                        zos.write(buff, 0, cnt);
                    }
                    zos.closeEntry();
                }
            }
            zos.close();
        }
    }
