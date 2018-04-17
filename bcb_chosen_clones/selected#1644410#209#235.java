    public boolean saveAsZip(String path) {
        if (!path.endsWith(".zip")) path = path + ".zip";
        if (name == null) name = imp.getTitle();
        if (name.endsWith(".zip")) name = name.substring(0, name.length() - 4);
        if (!name.endsWith(".tif")) name = name + ".tif";
        fi.description = getDescriptionString();
        Object info = imp.getProperty("Info");
        if (info != null && (info instanceof String)) fi.info = (String) info;
        fi.roi = RoiEncoder.saveAsByteArray(imp.getRoi());
        fi.overlay = getOverlay(imp);
        fi.sliceLabels = imp.getStack().getSliceLabels();
        if (imp.isComposite()) saveDisplayRangesAndLuts(imp, fi);
        if (fi.nImages > 1 && imp.getStack().isVirtual()) fi.virtualStack = (VirtualStack) imp.getStack();
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(path));
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(zos));
            zos.putNextEntry(new ZipEntry(name));
            TiffEncoder te = new TiffEncoder(fi);
            te.write(out);
            out.close();
        } catch (IOException e) {
            showErrorMessage(e);
            return false;
        }
        updateImp(fi, FileInfo.TIFF);
        return true;
    }
