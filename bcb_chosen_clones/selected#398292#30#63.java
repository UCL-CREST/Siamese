    @Override
    public JSONObject getJsonObj(Trip trip, List<Waypoint> stops) throws IOException {
        JSONObject jsonObject = null;
        try {
            List<String> filtered = new LinkedList<String>();
            filtered.add(trip.getWaypoint().getFromLocation().getCity().replaceAll("\\W+", "+"));
            filtered.add(trip.getWaypoint().getToLocation().getCity().replaceAll("\\W+", "+"));
            for (Waypoint w : stops) {
                String from = w.getFromLocation().getCity().replaceAll("\\W+", "+");
                if (!filtered.contains(from)) filtered.add(from);
                String to = w.getToLocation().getCity().replaceAll("\\W+", "+");
                if (!filtered.contains(to)) filtered.add(to);
            }
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("http://maps.google.com/maps/api/directions/json");
            urlBuilder.append("?origin=").append(filtered.get(0));
            urlBuilder.append("&destination=").append(filtered.get(1));
            if (filtered.size() > 2) {
                urlBuilder.append("&waypoints=");
                for (int i = 2; i < filtered.size() - 1; i++) urlBuilder.append(filtered.get(i)).append("|");
                urlBuilder.append(filtered.get(filtered.size() - 1));
            }
            urlBuilder.append("&sensor=false");
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(urlBuilder.toString()).openStream()));
            StringBuilder answerBuilder = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) answerBuilder.append(inputLine);
            in.close();
            jsonObject = new JSONObject(answerBuilder.toString());
        } catch (JSONException e) {
            logger.error("Problem with initializing JSONObject", e);
        }
        return jsonObject;
    }
