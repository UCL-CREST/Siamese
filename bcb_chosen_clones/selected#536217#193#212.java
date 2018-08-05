    private void publishZip(LWMap map) {
        try {
            if (map.getFile() == null) {
                VueUtil.alert(VueResources.getString("dialog.mapsave.message"), VueResources.getString("dialog.mapsave.title"));
                return;
            }
            File savedCMap = PublishUtil.createZip(map, Publisher.resourceVector);
            InputStream istream = new BufferedInputStream(new FileInputStream(savedCMap));
            OutputStream ostream = new BufferedOutputStream(new FileOutputStream(ActionUtil.selectFile("Export to Zip File", "zip")));
            int fileLength = (int) savedCMap.length();
            byte bytes[] = new byte[fileLength];
            while (istream.read(bytes, 0, fileLength) != -1) ostream.write(bytes, 0, fileLength);
            istream.close();
            ostream.close();
        } catch (Exception ex) {
            System.out.println(ex);
            VueUtil.alert(VUE.getDialogParent(), VueResources.getString("dialog.export.message") + ex.getMessage(), VueResources.getString("dialog.export.title"), JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
