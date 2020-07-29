    protected void EncodeRoute() throws EncodeFailedException, IOException {
        File storefile = getNode().getCodec().PackStore(null, ReturnStore);
        DownRoute.EncodeData(storefile);
        File data = getNode().getCodec().PackRemoteDownload(this);
        data = PigData.EncodeData(data);
        FileOutputStream fos = new FileOutputStream(PiggybackRouteFile, true);
        FileChannel foc = fos.getChannel();
        FileInputStream fis = new FileInputStream(data);
        FileChannel fic = fis.getChannel();
        fic.transferTo(0, fic.size(), foc);
        foc.close();
        fic.close();
        UpRoute.EncodeData(PiggybackRouteFile);
    }
