    public void zipFilemaker(Long ID) {
        MarketingRequest mr = (MarketingRequest) ObjectUtil.loadObject(uBean.getMarketingRequests(), Long.valueOf(ID));
        if (null == mr) return;
        MarketingPackage mp = mr.getMpackage();
        if (null == mp || null == mp.getID()) return;
        try {
            ApexFile f;
            String path = rootPath + "files" + File.separatorChar;
            byte[] buf = new byte[1024];
            Iterator<ApexFile> i = mp.getFiles().iterator();
            ZipOutputStream zf = new ZipOutputStream(new FileOutputStream(path + "mpackage_" + mp.getID() + ".zip"));
            while (i.hasNext()) {
                f = i.next();
                FileInputStream in = new FileInputStream(path + f.getFsname());
                zf.putNextEntry(new ZipEntry(f.getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    zf.write(buf, 0, len);
                }
                zf.closeEntry();
                in.close();
            }
            zf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
