    private static void pushFileEntry(File src, String root, ZipOutputStream zos, boolean verbos) throws Exception {
        String name = src.getCanonicalPath();
        name = CUtil.replaceString(name, root, "", 1);
        name = name.replaceAll("\\\\", "/");
        while (name.startsWith("/")) {
            name = name.substring(1);
        }
        byte[] data = CFile.readData(src);
        ZipEntry ze = new ZipEntry(name);
        ze.setTime(0);
        zos.putNextEntry(ze);
        zos.write(data);
        if (verbos) {
            System.out.println("put : " + CUtil.snapStringRightSize(data.length + "(bytes)", 22, ' ') + " " + name);
        }
    }
