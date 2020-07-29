    public static GoogleResponse getElevation(String lat, String lon) throws IOException {
        String url = "http://maps.google.com/maps/api/elevation/xml?locations=";
        url = url + String.valueOf(lat);
        url = url + ",";
        url = url + String.valueOf(lon);
        url = url + "&sensor=false";
        BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
        String line;
        GoogleResponse googleResponse = new GoogleResponse();
        googleResponse.lat = Double.valueOf(lat);
        googleResponse.lon = Double.valueOf(lon);
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.startsWith("<status>")) {
                line = line.replace("<status>", "");
                line = line.replace("</status>", "");
                googleResponse.status = line;
                if (!line.toLowerCase().equals("ok")) return googleResponse;
            } else if (line.startsWith("<elevation>")) {
                line = line.replace("<elevation>", "");
                line = line.replace("</elevation>", "");
                googleResponse.elevation = Double.valueOf(line);
                return googleResponse;
            }
        }
        return googleResponse;
    }
