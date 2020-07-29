    public void readDocument(URL url) {
        stopTiming();
        try {
            String xmlData = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            boolean cont = true;
            while (cont) {
                line = in.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (line.length() > 0 && line.charAt(0) != '%') {
                    xmlData = xmlData + line + System.getProperty("line.separator");
                }
                if (line.length() > 1 && line.charAt(0) == '%' && line.charAt(1) == '=') {
                    cont = false;
                }
            }
            XmlDataAdaptor readAdp = null;
            readAdp = XmlDataAdaptor.adaptorForString(xmlData, false);
            if (readAdp != null) {
                XmlDataAdaptor mpsfileData_Adaptor = readAdp.childAdaptor(dataRootName);
                if (mpsfileData_Adaptor != null) {
                    setTitle(mpsfileData_Adaptor.stringValue("title"));
                    java.util.Iterator<XmlDataAdaptor> plotIt = mpsfileData_Adaptor.childAdaptorIterator("Plot");
                    while (plotIt.hasNext()) {
                        XmlDataAdaptor pvDA = plotIt.next();
                        String name = pvDA.stringValue("name");
                        String xMin = pvDA.stringValue("xmin");
                        String xMax = pvDA.stringValue("xmax");
                        String step = pvDA.stringValue("step");
                        System.out.println(name + " " + xMax + " " + xMin + " " + step);
                        bModel.setPlotAxes(name, xMin, xMax, step);
                    }
                    java.util.Iterator<XmlDataAdaptor> timingIt = mpsfileData_Adaptor.childAdaptorIterator("TimingPV");
                    while (timingIt.hasNext()) {
                        XmlDataAdaptor pvDA = timingIt.next();
                        String name = pvDA.stringValue("name");
                        bModel.setTimingPVName(name);
                    }
                    java.util.Iterator<XmlDataAdaptor> trigIt = mpsfileData_Adaptor.childAdaptorIterator("Trigger");
                    while (trigIt.hasNext()) {
                        XmlDataAdaptor pvDA = trigIt.next();
                        String name = pvDA.stringValue("name");
                        String type = pvDA.stringValue("type");
                        bModel.addTrigger(name, type);
                    }
                    java.util.Iterator<XmlDataAdaptor> blmIt = mpsfileData_Adaptor.childAdaptorIterator("BLMdevice");
                    while (blmIt.hasNext()) {
                        XmlDataAdaptor pvDA = blmIt.next();
                        String name = pvDA.stringValue("name");
                        String section = pvDA.stringValue("section");
                        String mpschan = pvDA.stringValue("mpschan");
                        String devType = pvDA.stringValue("devicetype");
                        String location = pvDA.stringValue("locationz");
                        double locz = 0;
                        try {
                            locz = Double.parseDouble(location);
                        } catch (NumberFormatException e) {
                            locz = 0.0;
                        }
                        if (devType == null) bModel.addBLM(new IonizationChamber(name, section, mpschan, locz)); else if (devType.equals("ND")) bModel.addBLM(new NeutronDetector(name, section, mpschan, locz)); else if (devType.equals("IC")) bModel.addBLM(new IonizationChamber(name, section, mpschan, locz));
                    }
                }
            }
            in.close();
        } catch (IOException exception) {
            System.out.println("Fatal error. Something wrong with input file. Stop.");
        }
        startTiming();
    }
