    public SensorAgent load(String xmlFile) {
        SensorAgent sensorAgent = null;
        try {
            File f = new File(xmlFile);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(f);
            String name = doc.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();
            String id = doc.getElementsByTagName("id").item(0).getFirstChild().getNodeValue();
            String class_name = doc.getElementsByTagName("class").item(0).getFirstChild().getNodeValue();
            String location = doc.getElementsByTagName("location").item(0).getFirstChild().getNodeValue();
            String valueType = doc.getElementsByTagName("ValueType").item(0).getFirstChild().getNodeValue();
            String dataSchema = doc.getElementsByTagName("DataSchema").item(0).getAttributes().getNamedItem("type").getNodeValue();
            String dataDisseminateTime = doc.getElementsByTagName("DataDisseminate").item(0).getAttributes().getNamedItem("time").getNodeValue();
            String dataDisseminateLazy = doc.getElementsByTagName("DataDisseminate").item(0).getAttributes().getNamedItem("lazy").getNodeValue();
            ArrayList<String> args = new ArrayList<String>();
            Node arguments = doc.getElementsByTagName("arguments").item(0);
            for (Node argument = arguments.getFirstChild(); argument != null; argument = argument.getNextSibling()) {
                if (argument.getNodeType() == Node.ELEMENT_NODE) {
                    if (argument.getNodeName().equals("argument")) {
                        args.add(argument.getFirstChild().getNodeValue());
                    }
                }
            }
            Class<?> cons = Class.forName(class_name);
            Constructor<?> constructor = cons.getConstructor(Object.class);
            Object arg = Array.newInstance(String.class, args.size());
            for (int i = 0; i < args.size(); i++) Array.set(arg, i, args.get(i));
            Sensor sensor = (Sensor) constructor.newInstance(arg);
            DataDisseminate dataDisseminate = new DataDisseminate(dataSource, Integer.parseInt(dataDisseminateTime), Boolean.parseBoolean(dataDisseminateLazy));
            if (dataSchema.equals("push")) {
                sensorAgent = new PushSensorAgent(dataDisseminate, id, valueType, sensor);
            } else if (dataSchema.equals("pull")) {
            } else {
            }
            Thread t = new Thread(sensorAgent);
            t.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensorAgent;
    }
