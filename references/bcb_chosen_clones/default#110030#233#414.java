    private byte[] buildKMZ(String url, double begin, double end, double intv, int cnt, String imgname) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StringBuffer sb;
        String kmls;
        String photooverlays;
        String daes;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(new java.util.SimpleTimeZone(0, "UTC"));
        try {
            String line;
            sb = new StringBuffer();
            java.io.File file = new java.io.File(kmlFileName);
            java.io.FileReader fr = new java.io.FileReader(file);
            java.io.BufferedReader br = new java.io.BufferedReader(fr);
            while ((line = br.readLine()) != null) sb.append(line).append('\n');
            kmls = sb.toString();
            sb = new StringBuffer();
            file = new java.io.File(photooverlayFileName);
            fr = new java.io.FileReader(file);
            br = new java.io.BufferedReader(fr);
            while ((line = br.readLine()) != null) sb.append(line).append('\n');
            photooverlays = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String metaname = imgname.substring(0, imgname.lastIndexOf('.')) + ".xml";
        System.err.println("image channel: " + imgname);
        System.err.println("metadata channel: " + metaname);
        ChannelMap cm = new ChannelMap();
        ChannelMap cmin = null;
        try {
            cm.Add(imgname);
            cm.Add(metaname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String[] daedoc = new String[cnt];
            byte[][] image = new byte[cnt][];
            String[] stime = new String[cnt];
            if (cnt > 1) intv = (end - begin) / (2 * (cnt - 1));
            System.err.println("cnt " + cnt + ", intv " + intv);
            double lastetime = begin;
            double btime = begin;
            double etime = begin + intv;
            double lastitime = 0;
            for (int i = 0; i < cnt; i++) {
                double rtime = begin + 2 * i * intv;
                sink.Request(cm, rtime, 0, "absolute");
                cmin = sink.Fetch(4000);
                int imgidx = cmin.GetIndex(imgname);
                boolean writekml = true;
                if (imgidx > -1) {
                    image[i] = cmin.GetDataAsByteArray(cmin.GetIndex(imgname))[0];
                    System.err.println("i=" + i + ", imagelength=" + image[i].length);
                    if (i < cnt - 1 && cmin.GetTimeStart(imgidx) - lastitime < 1e-3) {
                        System.err.println("duplicate image!!");
                        writekml = false;
                    } else {
                        lastetime = etime;
                        lastitime = cmin.GetTimeStart(imgidx);
                    }
                } else {
                    System.err.println("NO IMAGE!!");
                    writekml = false;
                }
                if (writekml) {
                    btime = lastetime;
                    if (i == 0) btime = begin;
                    etime = begin + (2 * i + 1) * intv;
                    if (i == cnt - 1) etime = end;
                    String bdate = sdf.format(new java.util.Date((long) (btime * 1000)));
                    String edate = sdf.format(new java.util.Date((long) (etime * 1000)));
                    stime[i] = (new Long(Math.round(rtime * 10000))).toString();
                    byte[] metastring = null;
                    int idx = cmin.GetIndex(metaname);
                    if (idx > -1) {
                        if (cmin.GetType(idx) == ChannelMap.TYPE_BYTEARRAY) {
                            metastring = cmin.GetDataAsByteArray(idx)[0];
                        } else if (cmin.GetType(idx) == ChannelMap.TYPE_STRING) {
                            metastring = (cmin.GetDataAsString(idx)[0]).getBytes();
                        }
                    } else {
                        try {
                            java.io.File metafile = new java.io.File(metaname.substring(1 + metaname.lastIndexOf('/')));
                            sb = new StringBuffer();
                            String line = null;
                            java.io.FileReader fr = new java.io.FileReader(metafile);
                            java.io.BufferedReader br = new java.io.BufferedReader(fr);
                            while ((line = br.readLine()) != null) sb.append(line).append('\n');
                            metastring = sb.toString().getBytes();
                        } catch (Exception e) {
                            System.err.println("NO METADATA!!");
                            e.printStackTrace();
                        }
                    }
                    String docname;
                    String clongitude, clatitude, caltitude, ctilt, cheading, croll;
                    String vnear, vleftfov, vrightfov, vbottomfov, vtopfov;
                    String pcoordinates;
                    try {
                        javax.xml.parsers.DocumentBuilder db = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        java.io.ByteArrayInputStream meta = new java.io.ByteArrayInputStream(metastring);
                        org.w3c.dom.Element e = db.parse(meta).getDocumentElement();
                        org.w3c.dom.Element ee = (org.w3c.dom.Element) e.getElementsByTagName("name").item(0);
                        docname = ee.getFirstChild().getNodeValue();
                        ee = (org.w3c.dom.Element) e.getElementsByTagName("Camera").item(0);
                        clongitude = ee.getElementsByTagName("longitude").item(0).getFirstChild().getNodeValue();
                        clatitude = ee.getElementsByTagName("latitude").item(0).getFirstChild().getNodeValue();
                        caltitude = ee.getElementsByTagName("altitude").item(0).getFirstChild().getNodeValue();
                        ctilt = ee.getElementsByTagName("tilt").item(0).getFirstChild().getNodeValue();
                        cheading = ee.getElementsByTagName("heading").item(0).getFirstChild().getNodeValue();
                        croll = ee.getElementsByTagName("roll").item(0).getFirstChild().getNodeValue();
                        ee = (org.w3c.dom.Element) e.getElementsByTagName("ViewVolume").item(0);
                        vnear = ee.getElementsByTagName("near").item(0).getFirstChild().getNodeValue();
                        vleftfov = ee.getElementsByTagName("leftFov").item(0).getFirstChild().getNodeValue();
                        vrightfov = ee.getElementsByTagName("rightFov").item(0).getFirstChild().getNodeValue();
                        vbottomfov = ee.getElementsByTagName("bottomFov").item(0).getFirstChild().getNodeValue();
                        vtopfov = ee.getElementsByTagName("topFov").item(0).getFirstChild().getNodeValue();
                        ee = (org.w3c.dom.Element) e.getElementsByTagName("Point").item(0);
                        pcoordinates = ee.getElementsByTagName("coordinates").item(0).getFirstChild().getNodeValue();
                    } catch (Exception e) {
                        System.err.println("Exception parsing metadata!");
                        e.printStackTrace();
                        return null;
                    }
                    if (i == 0) {
                    }
                    String po = photooverlays.replaceFirst("##name##", docname);
                    po = po.replaceFirst("##begin##", bdate);
                    po = po.replaceFirst("##end##", edate);
                    po = po.replaceFirst("##clongitude##", clongitude);
                    po = po.replaceFirst("##clatitude##", clatitude);
                    po = po.replaceFirst("##caltitude##", caltitude);
                    po = po.replaceFirst("##ctilt##", ctilt);
                    po = po.replaceFirst("##cheading##", cheading);
                    po = po.replaceFirst("##croll##", croll);
                    po = po.replaceFirst("##href-jpg##", "i" + stime[i] + ".jpg");
                    po = po.replaceFirst("##vnear##", vnear);
                    po = po.replaceFirst("##vleftfov##", vleftfov);
                    po = po.replaceFirst("##vrightfov##", vrightfov);
                    po = po.replaceFirst("##vbottomfov##", vbottomfov);
                    po = po.replaceFirst("##vtopfov##", vtopfov);
                    po = po.replaceAll("##coordinates##", pcoordinates);
                    kmls = kmls.replaceFirst("##photooverlay##", po + "##photooverlay##");
                }
            }
            kmls = kmls.replaceFirst("##photooverlay##", "");
            ZipOutputStream zos = new ZipOutputStream(baos);
            zos.setMethod(ZipOutputStream.DEFLATED);
            zos.setLevel(Deflater.DEFAULT_COMPRESSION);
            ZipEntry ze = new ZipEntry("foo.kml");
            zos.putNextEntry(ze);
            byte[] data = kmls.getBytes();
            zos.write(data, 0, data.length);
            zos.closeEntry();
            for (int i = 0; i < cnt; i++) {
                if (daedoc[i] != null) {
                    ze = new ZipEntry("d" + stime[i] + ".dae");
                    zos.putNextEntry(ze);
                    data = daedoc[i].getBytes();
                    zos.write(data, 0, data.length);
                    zos.closeEntry();
                }
            }
            for (int i = 0; i < cnt; i++) {
                if (stime[i] != null) {
                    ze = new ZipEntry("i" + stime[i] + ".jpg");
                    zos.putNextEntry(ze);
                    data = image[i];
                    zos.write(data, 0, data.length);
                    zos.closeEntry();
                }
            }
            zos.close();
        } catch (Exception e) {
            System.err.println("Exception zipping kml/dae documents");
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
