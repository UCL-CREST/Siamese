    public void doZip() {
        String filename = "";
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(outputfilename);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER];
            Set<String> keys = files.keySet();
            for (String key : keys) {
                filename = key;
                String fpath = files.get(key);
                FileInputStream fi = new FileInputStream(fpath);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(key);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
            zipComplete = true;
        } catch (FileNotFoundException e) {
            Activator.getDefault().showMessage(bundle.getString("Missing_File") + ": " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
