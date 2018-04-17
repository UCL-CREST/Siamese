    boolean saveMultiple(int[] indexes, String path) {
        Macro.setOptions(null);
        if (path == null) {
            SaveDialog sd = new SaveDialog("Save ROIs...", "RoiSet", ".zip");
            String name = sd.getFileName();
            if (name == null) return false;
            if (!(name.endsWith(".zip") || name.endsWith(".ZIP"))) name = name + ".zip";
            String dir = sd.getDirectory();
            path = dir + name;
        }
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(path));
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(zos));
            RoiEncoder re = new RoiEncoder(out);
            for (int i = 0; i < indexes.length; i++) {
                String label = list.getItem(indexes[i]);
                Roi roi = (Roi) rois.get(label);
                if (!label.endsWith(".roi")) label += ".roi";
                zos.putNextEntry(new ZipEntry(label));
                re.write(roi);
                out.flush();
            }
            out.close();
        } catch (IOException e) {
            error("" + e);
            return false;
        }
        if (record()) Recorder.record("roiManager", "Save", path);
        return true;
    }
