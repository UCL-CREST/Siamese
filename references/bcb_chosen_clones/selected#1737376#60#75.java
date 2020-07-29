    private void addParameters(ZipOutputStream zos, NucleiMgr nucleiMgr) throws IOException {
        ZipNuclei zn = nucleiMgr.getZipNuclei();
        String ename = nucleiMgr.getParameterEntry();
        zos.putNextEntry(new ZipEntry(ename));
        Vector parameterFileInfo = nucleiMgr.getParameterFileInfo();
        String s = null;
        if (parameterFileInfo != null) {
            for (int i = 0; i < parameterFileInfo.size(); i++) {
                s = (String) parameterFileInfo.elementAt(i);
                s += "\n";
                byte[] b = s.getBytes();
                zos.write(b, 0, b.length);
            }
        }
        zos.closeEntry();
    }
