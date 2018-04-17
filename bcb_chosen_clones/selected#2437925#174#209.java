    public static Class<?> getDexClass(String name, byte[] data) throws IOException {
        File fff = new File("/tmp/jvm.class");
        if (!fff.exists()) {
            fff.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(fff);
        fos.write(data);
        fos.close();
        com.google.dex.file.DexFile outputDex = new com.google.dex.file.DexFile();
        CfOptions cf = new CfOptions();
        ClassDefItem clazz = CfTranslator.translate(fixPath(name.replace('.', '/') + ".class"), data, cf);
        outputDex.add(clazz);
        File tmpdir = new File(tmpdirpath + name);
        if (!tmpdir.exists()) {
            tmpdir.mkdir();
        } else {
            if (!tmpdir.isDirectory()) {
                throw new IOException();
            }
        }
        File apk = new File(tmpdirpath + name + "/" + name + ".apk");
        if (!apk.exists()) {
            apk.createNewFile();
        }
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(apk));
        ZipEntry classeszip = new ZipEntry("classes.dex");
        zos.putNextEntry(classeszip);
        outputDex.writeTo(zos, null, false);
        zos.closeEntry();
        zos.close();
        getClassByName(apppath, "org/python/core/PyFunctionTable");
        getClassByName(apppath, "org/python/core/PyRunnable");
        Class<?> c = getClassByName(tmpdirpath + name + "/" + name + ".apk", name.replace('.', '/'));
        getClassByName(apppath, "org/python/core/PyFunctionTable");
        return c;
    }
