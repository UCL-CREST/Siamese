    protected long getCheckSum(Node node) {
        long timing = System.currentTimeMillis();
        CRC32 chksum = new CRC32();
        try {
            Transformer transformer = tFactory.newTransformer();
            DOMSource checkSource = new DOMSource(node);
            ByteArrayOutputStream checkBytes = new ByteArrayOutputStream();
            StreamResult checkResult = new StreamResult(checkBytes);
            transformer.transform(checkSource, checkResult);
            checkBytes.close();
            if (DEBUG >= 3) {
                checkBytes.writeTo(new DataOutputStream(new FileOutputStream("tttreadBytes.xml")));
            }
            chksum.update(checkBytes.toByteArray());
        } catch (TransformerConfigurationException tce) {
            logger.severe("\n** Transformer Factory error");
            logger.severe("   " + tce.getMessage());
            Throwable x = tce;
            if (tce.getException() != null) {
                x = tce.getException();
            }
            logger.log(Level.SEVERE, "", x);
        } catch (TransformerException te) {
            logger.severe("\n** Transformation error");
            logger.severe("   " + te.getMessage());
            Throwable x = te;
            if (te.getException() != null) {
                x = te.getException();
            }
            logger.log(Level.SEVERE, "", x);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
        timing = System.currentTimeMillis() - timing;
        if (TIMING > 2) {
            logger.info("TIMING[3]: checksum calculated in " + timing + " milliseconds");
        }
        return chksum.getValue();
    }
