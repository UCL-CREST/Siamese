    public void addFile(String addFile, String sourceFile) {
        boolean exists = (new File(filename)).exists();
        if (!exists) {
            try {
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(filename));
                FileInputStream in = new FileInputStream(sourceFile);
                byte buf[] = new byte[1024];
                out.putNextEntry(new ZipEntry(addFile));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
                out.close();
                Debug.debug("Added file '" + sourceFile + "' to archive '" + filename + "' as file '" + addFile + "'");
            } catch (IOException e) {
                Debug.debug("Unable to add file '" + sourceFile + "' to archive '" + filename + "': " + e.getMessage());
            }
        } else {
            File tmpFile = null, jarFile = null;
            FileOutputStream out = null;
            ZipOutputStream jos = null;
            FileInputStream fis = null;
            ZipInputStream jis = null;
            BufferedReader br = null;
            BufferedWriter bw = null;
            try {
                tmpFile = new File(filename + ".tmp");
                jarFile = new File(filename);
                out = new FileOutputStream(tmpFile);
                jos = new ZipOutputStream(out);
                fis = new FileInputStream(jarFile);
                jis = new ZipInputStream(fis);
                br = new BufferedReader(new InputStreamReader(jis));
                bw = new BufferedWriter(new OutputStreamWriter(jos));
            } catch (IOException e) {
                Debug.debug("Unable to add to zip file: " + e.getMessage());
                return;
            }
            try {
                ZipEntry ze = null;
                while ((ze = jis.getNextEntry()) != null) {
                    jos.putNextEntry(ze);
                    int buf = -1;
                    while ((buf = br.read()) != -1) {
                        bw.write(buf);
                    }
                    bw.flush();
                    jos.closeEntry();
                }
            } catch (Exception e) {
                Debug.debug("Exception: " + e);
            }
            try {
                FileInputStream in = new FileInputStream(sourceFile);
                byte buf[] = new byte[1024];
                jos.putNextEntry(new ZipEntry(addFile));
                int len;
                while ((len = in.read(buf)) > 0) {
                    jos.write(buf, 0, len);
                }
                jos.closeEntry();
                in.close();
                jos.close();
                out.close();
                fis.close();
                jis.close();
                br.close();
                bw.close();
                Debug.debug("Added file '" + sourceFile + "' to archive '" + filename + "' as file '" + addFile + "'");
            } catch (IOException e) {
                Debug.debug("Unable to add file '" + sourceFile + "' to archive '" + filename + "': " + e.getMessage());
            }
            try {
                jarFile.delete();
                tmpFile.renameTo(jarFile);
            } catch (Exception e) {
                Debug.debug("Unable to rename '" + jarFile.getName() + "' to '" + tmpFile.getName() + "'");
            }
            tmpFile = null;
            jarFile = null;
            out = null;
            jos = null;
            fis = null;
            jis = null;
            br = null;
            bw = null;
        }
    }
