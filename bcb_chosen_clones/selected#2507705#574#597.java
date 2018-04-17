    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {
        String key = null;
        org.unicef.doc.ibis.nut.persistence.File ifile = null;
        java.io.File ofile = null;
        java.io.FileOutputStream fout = null;
        java.awt.Desktop theDesktop = null;
        if (!java.awt.Desktop.isDesktopSupported() || this.image == null || !image.isSetMedia() || !image.getMedia().isSetContent() || !image.getMedia().isSetName()) {
            return;
        }
        key = image.getMedia().getName();
        try {
            theDesktop = java.awt.Desktop.getDesktop();
            ofile = new java.io.File(System.getProperty("java.io.tmpdir") + System.getProperty("file.separator") + java.util.UUID.randomUUID().toString() + key);
            ofile.deleteOnExit();
            fout = new java.io.FileOutputStream(ofile);
            ifile = image.getMedia();
            fout.write(ifile.getContent());
            fout.flush();
            fout.close();
            theDesktop.open(ofile);
        } catch (IOException ioe) {
            Logger.getLogger(ImageDisplayPanel.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }
