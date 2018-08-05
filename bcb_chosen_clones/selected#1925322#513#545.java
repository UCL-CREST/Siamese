    private void copyImage(ProjectElement e) throws Exception {
        String fn = e.getName();
        if (!fn.toLowerCase().endsWith(".png")) {
            if (fn.contains(".")) {
                fn = fn.substring(0, fn.lastIndexOf('.')) + ".png";
            } else {
                fn += ".png";
            }
        }
        File img = new File(resFolder, fn);
        File imgz = new File(resoutFolder.getAbsolutePath(), fn + ".zlib");
        boolean copy = true;
        if (img.exists() && config.containsKey(img.getName())) {
            long modified = Long.parseLong(config.get(img.getName()));
            if (modified >= img.lastModified()) {
                copy = false;
            }
        }
        if (copy) {
            convertImage(e.getFile(), img);
            config.put(img.getName(), String.valueOf(img.lastModified()));
        }
        DeflaterOutputStream out = new DeflaterOutputStream(new BufferedOutputStream(new FileOutputStream(imgz)));
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(img));
        int read;
        while ((read = in.read()) != -1) {
            out.write(read);
        }
        out.close();
        in.close();
        imageFiles.add(imgz);
        imageNames.put(imgz, e.getName());
    }
