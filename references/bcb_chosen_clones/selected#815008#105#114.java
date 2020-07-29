    public void setPanelsOrder(ArrayList order) throws Exception {
        sendMsg("Setting the panels order ...");
        outJar.putNextEntry(new ZipEntry("panelsOrder"));
        DataOutputStream datOut = new DataOutputStream(outJar);
        int size = order.size();
        datOut.writeInt(size);
        for (int i = 0; i < size; i++) datOut.writeUTF((String) order.get(i));
        datOut.flush();
        outJar.closeEntry();
    }
