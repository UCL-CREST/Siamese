    private void addPNMLFileToLibrary(File selected) {
        try {
            FileChannel srcChannel = new FileInputStream(selected.getAbsolutePath()).getChannel();
            FileChannel dstChannel = new FileOutputStream(new File(matchingOrderXML).getParent() + "/" + selected.getName()).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            dstChannel.close();
            order.add(new ComponentDescription(false, selected.getName().replaceAll(".pnml", ""), 1.0));
            updateComponentList();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(dialog, "Could not add the PNML file " + selected.getName() + " to the library!");
        }
    }
