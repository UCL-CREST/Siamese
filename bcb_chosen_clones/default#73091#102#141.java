    public Document convertKMLtoGPX(String kml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document kmldoc = builder.parse(new InputSource(new StringReader(kml)));
        Document gpxdoc = builder.newDocument();
        String comment = "Converted by httpunit GoogleMapsExample";
        gpxdoc.appendChild(gpxdoc.createComment(comment));
        org.w3c.dom.Element root = gpxdoc.createElement("gpx");
        root.setAttribute("xmlns", "http://www.topografix.com/GPX/1/1");
        root.setAttribute("creator", "KML2GPX Example by BITPlan");
        root.setAttribute("version", "1.1");
        gpxdoc.appendChild(root);
        org.w3c.dom.Element metadata = gpxdoc.createElement("metadata");
        org.w3c.dom.Element metadatalink = gpxdoc.createElement("link");
        metadatalink.setAttribute("href", "http://www.bitplan.com");
        metadata.appendChild(metadatalink);
        org.w3c.dom.Element metadatatext = gpxdoc.createElement("text");
        metadatatext.setTextContent("BITPlan GmbH, Willich, Germany");
        metadatalink.appendChild(metadatatext);
        root.appendChild(metadata);
        org.w3c.dom.Element route = gpxdoc.createElement("rte");
        root.appendChild(route);
        NodeList routePoints = kmldoc.getElementsByTagName("Placemark");
        for (int i = 0; i < routePoints.getLength(); i++) {
            Element kmlRoutePoint = (Element) routePoints.item(i);
            String name = getSubNode(kmlRoutePoint, "name", true).getTextContent();
            if (DEBUG) System.out.println("found route point " + i + ": " + name);
            String coords[] = extractCoordinates(kmlRoutePoint);
            if (coords != null) {
                org.w3c.dom.Element routePoint = gpxdoc.createElement("rtept");
                routePoint.setAttribute("lon", coords[0]);
                routePoint.setAttribute("lat", coords[1]);
                org.w3c.dom.Element routePointName = gpxdoc.createElement("name");
                routePointName.setTextContent(name);
                routePoint.appendChild(routePointName);
                route.appendChild(routePoint);
            }
        }
        return gpxdoc;
    }
