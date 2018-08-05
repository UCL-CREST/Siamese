    public static void joinFiles(FileValidator validator, File target, File[] sources) {
        FileOutputStream fos = null;
        try {
            if (!validator.verifyFile(target)) return;
            fos = new FileOutputStream(target);
            FileInputStream fis = null;
            byte[] bytes = new byte[512];
            for (int i = 0; i < sources.length; i++) {
                fis = new FileInputStream(sources[i]);
                int nbread = 0;
                try {
                    while ((nbread = fis.read(bytes)) > -1) {
                        fos.write(bytes, 0, nbread);
                    }
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(null, ioe, i18n.getString("Failure"), JOptionPane.ERROR_MESSAGE);
                } finally {
                    fis.close();
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
    }
