    void writeRawDataFile(RawDataFileImpl rawDataFile, int number) throws IOException, TransformerConfigurationException, SAXException {
        numOfScans = rawDataFile.getNumOfScans();
        dataPointsOffsets = rawDataFile.getDataPointsOffsets();
        dataPointsLengths = rawDataFile.getDataPointsLengths();
        consolidatedDataPointsOffsets = new TreeMap<Integer, Long>();
        logger.info("Saving data points of: " + rawDataFile.getName());
        String rawDataSavedName = "Raw data file #" + number + " " + rawDataFile.getName();
        zipOutputStream.putNextEntry(new ZipEntry(rawDataSavedName + ".scans"));
        long newOffset = 0;
        byte buffer[] = new byte[1 << 20];
        RandomAccessFile dataPointsFile = rawDataFile.getDataPointsFile();
        for (Integer storageID : dataPointsOffsets.keySet()) {
            if (canceled) return;
            final long offset = dataPointsOffsets.get(storageID);
            dataPointsFile.seek(offset);
            final int bytes = dataPointsLengths.get(storageID) * 4 * 2;
            consolidatedDataPointsOffsets.put(storageID, newOffset);
            if (buffer.length < bytes) {
                buffer = new byte[bytes * 2];
            }
            dataPointsFile.read(buffer, 0, bytes);
            zipOutputStream.write(buffer, 0, bytes);
            newOffset += bytes;
            progress = 0.9 * ((double) offset / dataPointsFile.length());
        }
        if (canceled) return;
        logger.info("Saving raw data description of: " + rawDataFile.getName());
        zipOutputStream.putNextEntry(new ZipEntry(rawDataSavedName + ".xml"));
        OutputStream finalStream = zipOutputStream;
        StreamResult streamResult = new StreamResult(finalStream);
        SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        TransformerHandler hd = tf.newTransformerHandler();
        Transformer serializer = hd.getTransformer();
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        hd.setResult(streamResult);
        hd.startDocument();
        saveRawDataInformation(rawDataFile, hd);
        hd.endDocument();
    }
