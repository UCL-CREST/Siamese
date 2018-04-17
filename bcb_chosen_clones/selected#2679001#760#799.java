    private void jbViewFileActionPerformed(java.awt.event.ActionEvent evt) {
        String key = null;
        org.unicef.doc.ibis.nut.persistence.File ifile = null;
        java.io.File ofile = null;
        java.io.FileOutputStream fout = null;
        java.awt.Desktop theDesktop = null;
        if (jlMedia.getSelectedIndex() < 0) {
            return;
        }
        if (!java.awt.Desktop.isDesktopSupported()) {
            return;
        }
        try {
            key = (String) jlMedia.getSelectedValue();
            theDesktop = java.awt.Desktop.getDesktop();
            ifile = fMediaMap.get(key);
            if (ifile != null && ifile.isSetURI() && !ifile.isSetContent()) {
                theDesktop.browse(new java.net.URI(ifile.getURI()));
                return;
            }
            ofile = new java.io.File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + java.util.UUID.randomUUID().toString() + key);
            ofile.deleteOnExit();
            fout = new java.io.FileOutputStream(ofile);
            fout.write(ifile.getContent());
            fout.flush();
            fout.close();
            theDesktop.browse(ofile.toURI());
        } catch (IOException ioe) {
            Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, ioe);
        } catch (RuntimeException rte) {
            Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, rte);
        } catch (Exception e) {
            Logger.getLogger(AudioPanel.class.getName()).log(Level.SEVERE, null, e);
        }
        key = null;
        ifile = null;
        ofile = null;
        fout = null;
        theDesktop = null;
    }
