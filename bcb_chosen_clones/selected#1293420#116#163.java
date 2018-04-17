    public void updateCoordinates(Address address) {
        String mapURL = "http://maps.google.com/maps/geo?output=csv";
        String mapKey = "ABQIAAAAi__aT6y6l86JjbootR-p9xQd1nlEHNeAVGWQhS84yIVN5yGO2RQQPg9QLzy82PFlCzXtMNe6ofKjnA";
        String location = address.getStreet() + " " + address.getZip() + " " + address.getCity();
        if (logger.isDebugEnabled()) {
            logger.debug(location);
        }
        double[] coordinates = { 0.0, 0.0 };
        String content = "";
        try {
            location = URLEncoder.encode(location, "UTF-8");
            String request = mapURL + "&q=" + location + "&key=" + mapKey;
            URL url = new URL(request);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                content += line;
            }
            reader.close();
        } catch (Exception e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Error from google: " + e.getMessage());
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug(content);
        }
        StringTokenizer tokenizer = new StringTokenizer(content, ",");
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            i++;
            String token = tokenizer.nextToken();
            if (i == 3) {
                coordinates[0] = Double.parseDouble(token);
            }
            if (i == 4) {
                coordinates[1] = Double.parseDouble(token);
            }
        }
        if ((coordinates[0] != 0) || (coordinates[1] != 0)) {
            address.setLatitude(coordinates[0]);
            address.setLongitude(coordinates[1]);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Invalid coordinates for address " + address.getId());
            }
        }
    }
