    protected void processRequest(ChannelMap fwdData, PlugInChannelMap out) throws SAPIException {
        String[] chanList = out.GetChannelList();
        String requestChanStr = chanList[0];
        if (requestChanStr.endsWith("/")) {
            requestChanStr = requestChanStr.substring(0, requestChanStr.length() - 1);
        }
        System.err.println((new Date()).toString() + "  Source: " + requestChanStr);
        ChannelMap reqMap = new ChannelMap();
        reqMap.Add(requestChanStr + "/Altitude");
        reqMap.Add(requestChanStr + "/Latitude");
        reqMap.Add(requestChanStr + "/Longitude");
        reqMap.Add(requestChanStr + "/GroundSpeed");
        reqMap.Add(requestChanStr + "/Heading");
        sink.Request(reqMap, 0, 0, "newest");
        ChannelMap dataMap = sink.Fetch(60000);
        int altIndex = dataMap.GetIndex(requestChanStr + "/Altitude");
        double alt = 0.0;
        if (dataMap.GetType(altIndex) == ChannelMap.TYPE_FLOAT64) {
            alt = dataMap.GetDataAsFloat64(altIndex)[0];
        } else if (dataMap.GetType(altIndex) == ChannelMap.TYPE_FLOAT32) {
            alt = (double) dataMap.GetDataAsFloat32(altIndex)[0];
        }
        int latIndex = dataMap.GetIndex(requestChanStr + "/Latitude");
        double lat = 0.0;
        if (dataMap.GetType(latIndex) == ChannelMap.TYPE_FLOAT64) {
            lat = dataMap.GetDataAsFloat64(latIndex)[0];
        } else if (dataMap.GetType(latIndex) == ChannelMap.TYPE_FLOAT32) {
            lat = (double) dataMap.GetDataAsFloat32(latIndex)[0];
        }
        int lonIndex = dataMap.GetIndex(requestChanStr + "/Longitude");
        double lon = 0.0;
        if (dataMap.GetType(lonIndex) == ChannelMap.TYPE_FLOAT64) {
            lon = dataMap.GetDataAsFloat64(lonIndex)[0];
        } else if (dataMap.GetType(lonIndex) == ChannelMap.TYPE_FLOAT32) {
            lon = (double) dataMap.GetDataAsFloat32(lonIndex)[0];
        }
        int gsIndex = dataMap.GetIndex(requestChanStr + "/GroundSpeed");
        double gs = 0.0;
        if (dataMap.GetType(gsIndex) == ChannelMap.TYPE_FLOAT64) {
            gs = dataMap.GetDataAsFloat64(gsIndex)[0];
        } else if (dataMap.GetType(gsIndex) == ChannelMap.TYPE_FLOAT32) {
            gs = (double) dataMap.GetDataAsFloat32(gsIndex)[0];
        }
        int headIndex = dataMap.GetIndex(requestChanStr + "/Heading");
        double heading = 0.0;
        if (dataMap.GetType(headIndex) == ChannelMap.TYPE_FLOAT64) {
            heading = dataMap.GetDataAsFloat64(headIndex)[0];
        } else if (dataMap.GetType(headIndex) == ChannelMap.TYPE_FLOAT32) {
            heading = (double) dataMap.GetDataAsFloat32(headIndex)[0];
        }
        double dataTime = dataMap.GetTimes(altIndex)[0];
        double currTime = System.currentTimeMillis() / 1000.0;
        double latency = currTime - dataTime;
        boolean bMaxLatencyExceeded = false;
        if (latency > maxLatency) {
            latency = maxLatency;
            bMaxLatencyExceeded = true;
        }
        double radius = latency * gs;
        String kmlStr = createDRCircle(lat, lon, alt, radius, heading, bMaxLatencyExceeded);
        out.PutTime(System.currentTimeMillis() / 1000.0, out.GetRequestDuration());
        if (!bKMZ) {
            out.PutDataAsString(0, kmlStr);
            out.PutMime(0, "application/vnd.google-earth.kml+xml");
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ZipOutputStream zos = new ZipOutputStream(baos);
                ZipEntry ze = new ZipEntry("doc.kml");
                zos.setMethod(ZipOutputStream.DEFLATED);
                zos.setLevel(Deflater.DEFAULT_COMPRESSION);
                zos.putNextEntry(ze);
                byte[] kmlBytes = kmlStr.getBytes();
                zos.write(kmlBytes, 0, kmlBytes.length);
                zos.close();
                out.PutDataAsByteArray(0, baos.toByteArray());
                out.PutMime(0, "application/vnd.google-earth.kmz");
            } catch (Exception ex) {
                System.err.println("Exception generating KMZ: " + ex.getMessage());
                throw new SAPIException("Exception generating KMZ");
            }
        }
    }
