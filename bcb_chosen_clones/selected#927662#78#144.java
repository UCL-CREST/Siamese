    public static File[] splitFile(FileValidator validator, File source, long target_length, File todir, String prefix) {
        if (target_length == 0) return null;
        if (todir == null) {
            todir = new File(System.getProperty("java.io.tmpdir"));
        }
        if (prefix == null || prefix.equals("")) {
            prefix = source.getName();
        }
        Vector result = new Vector();
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(source);
            byte[] bytes = new byte[CACHE_SIZE];
            long current_target_size = 0;
            int current_target_nb = 1;
            int nbread = -1;
            try {
                File f = new File(todir, prefix + i18n.getString("targetname_suffix") + current_target_nb);
                if (!validator.verifyFile(f)) return null;
                result.add(f);
                fos = new FileOutputStream(f);
                while ((nbread = fis.read(bytes)) > -1) {
                    if ((current_target_size + nbread) > target_length) {
                        int limit = (int) (target_length - current_target_size);
                        fos.write(bytes, 0, limit);
                        fos.close();
                        current_target_nb++;
                        current_target_size = 0;
                        f = new File(todir, prefix + "_" + current_target_nb);
                        if (!validator.verifyFile(f)) return null;
                        result.add(f);
                        fos = new FileOutputStream(f);
                        fos.write(bytes, limit, nbread - limit);
                        current_target_size += nbread - limit;
                    } else {
                        fos.write(bytes, 0, nbread);
                        current_target_size += nbread;
                    }
                }
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(null, ioe, i18n.getString("Failure"), JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    if (fos != null) fos.close();
                } catch (IOException e) {
                }
                try {
                    if (fis != null) fis.close();
                } catch (IOException e) {
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, i18n.getString("Failure"), JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
        File[] fresult = null;
        if (result.size() > 0) {
            fresult = new File[result.size()];
            fresult = (File[]) result.toArray(fresult);
        }
        return fresult;
    }
