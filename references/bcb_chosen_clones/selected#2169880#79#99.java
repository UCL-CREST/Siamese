    protected File EncodeReturn() throws EncodeFailedException, IOException {
        CryptoClient c = getNode().getCryptoClientByID(PiggybackCrypto);
        File tmpf = getNode().getTempFM().createNewFile("encodereturn", "download");
        ChannelWriter cw = new ChannelWriter(tmpf);
        cw.putLongFile(DownloadData);
        cw.close();
        File encdata = c.RawEncode(tmpf, RawKey);
        File pigdata = PigData.EncodeData(encdata);
        File pigroute = ReturnPigRoute.EncodeData(ReturnRouteFile);
        FileOutputStream fos = new FileOutputStream(pigroute, true);
        FileChannel foc = fos.getChannel();
        FileInputStream fis = new FileInputStream(pigdata);
        FileChannel fic = fis.getChannel();
        fic.transferTo(0, fic.size(), foc);
        foc.close();
        fic.close();
        pigdata.delete();
        ReturnRouteFile.delete();
        encdata.delete();
        return pigroute;
    }
