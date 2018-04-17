    private void publishCMap(LWMap map) throws IOException {
        try {
            File savedCMap = PublishUtil.createIMSCP(Publisher.resourceVector);
            InputStream istream = new BufferedInputStream(new FileInputStream(savedCMap));
            OutputStream ostream = new BufferedOutputStream(new FileOutputStream(ActionUtil.selectFile("IMSCP", "zip")));
            int fileLength = (int) savedCMap.length();
            byte bytes[] = new byte[fileLength];
            while (istream.read(bytes, 0, fileLength) != -1) ostream.write(bytes, 0, fileLength);
            istream.close();
            ostream.close();
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            System.out.println(ex);
            VueUtil.alert(VUE.getDialogParent(), VueResources.getString("dialog.export.message") + ex.getMessage(), VueResources.getString("dialog.export.title"), JOptionPane.ERROR_MESSAGE);
        }
    }
