    private static void readAndRewrite(File inFile, File outFile) throws IOException {
        ImageInputStream iis = ImageIO.createImageInputStream(new BufferedInputStream(new FileInputStream(inFile)));
        DcmParser dcmParser = DcmParserFactory.getInstance().newDcmParser(iis);
        Dataset ds = DcmObjectFactory.getInstance().newDataset();
        dcmParser.setDcmHandler(ds.getDcmHandler());
        dcmParser.parseDcmFile(null, Tags.PixelData);
        PixelDataReader pdReader = pdFact.newReader(ds, iis, dcmParser.getDcmDecodeParam().byteOrder, dcmParser.getReadVR());
        System.out.println("reading " + inFile + "...");
        pdReader.readPixelData(false);
        ImageOutputStream out = ImageIO.createImageOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)));
        DcmEncodeParam dcmEncParam = DcmEncodeParam.IVR_LE;
        ds.writeDataset(out, dcmEncParam);
        ds.writeHeader(out, dcmEncParam, Tags.PixelData, dcmParser.getReadVR(), dcmParser.getReadLength());
        System.out.println("writing " + outFile + "...");
        PixelDataWriter pdWriter = pdFact.newWriter(pdReader.getPixelDataArray(), false, ds, out, dcmParser.getDcmDecodeParam().byteOrder, dcmParser.getReadVR());
        pdWriter.writePixelData();
        out.flush();
        out.close();
        System.out.println("done!");
    }
