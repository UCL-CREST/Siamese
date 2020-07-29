    boolean copyFileStructure(File oldFile, File newFile) {
        if (oldFile == null || newFile == null) return false;
        File searchFile = newFile;
        do {
            if (oldFile.equals(searchFile)) return false;
            searchFile = searchFile.getParentFile();
        } while (searchFile != null);
        if (oldFile.isDirectory()) {
            if (progressDialog != null) {
                progressDialog.setDetailFile(oldFile, ProgressDialog.COPY);
            }
            if (simulateOnly) {
            } else {
                if (!newFile.mkdirs()) return false;
            }
            File[] subFiles = oldFile.listFiles();
            if (subFiles != null) {
                if (progressDialog != null) {
                    progressDialog.addWorkUnits(subFiles.length);
                }
                for (int i = 0; i < subFiles.length; i++) {
                    File oldSubFile = subFiles[i];
                    File newSubFile = new File(newFile, oldSubFile.getName());
                    if (!copyFileStructure(oldSubFile, newSubFile)) return false;
                    if (progressDialog != null) {
                        progressDialog.addProgress(1);
                        if (progressDialog.isCancelled()) return false;
                    }
                }
            }
        } else {
            if (simulateOnly) {
            } else {
                FileReader in = null;
                FileWriter out = null;
                try {
                    in = new FileReader(oldFile);
                    out = new FileWriter(newFile);
                    int count;
                    while ((count = in.read()) != -1) out.write(count);
                } catch (FileNotFoundException e) {
                    return false;
                } catch (IOException e) {
                    return false;
                } finally {
                    try {
                        if (in != null) in.close();
                        if (out != null) out.close();
                    } catch (IOException e) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
