    public static void insertModuleinfo(ZipOutputStream dest, ModuleInfo info) throws IOException {
        ZipEntry zse = new ZipEntry(ModuleInfo.MODULEINFO_RAK);
        dest.putNextEntry(zse);
        info.write(dest);
        dest.flush();
    }
