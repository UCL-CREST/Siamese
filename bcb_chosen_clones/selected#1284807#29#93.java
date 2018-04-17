    @SuppressWarnings("unchecked")
    public List<Event> lookupFutureEvents(String groupIdentifier) throws GtugsException {
        StringBuilder json = new StringBuilder();
        String requestUrl = "http://api.meetup.com/events.json/?group_urlname=" + groupIdentifier + "&key=" + key;
        try {
            URL url = new URL(requestUrl.toString());
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            throw new GtugsException(e);
        }
        List<Event> events = new ArrayList<Event>();
        JSONObject jsonObj = (JSONObject) JSONValue.parse(json.toString());
        JSONArray results = (JSONArray) jsonObj.get("results");
        for (int i = 0; i < results.size(); i++) {
            JSONObject result = (JSONObject) results.get(i);
            Event e = new Event();
            e.setAttendeeCount(Integer.parseInt((String) result.get("rsvpcount")));
            e.setCity((String) result.get("venue_city"));
            e.setDescription((String) result.get("description"));
            e.setLatitude(Double.parseDouble((String) result.get("venue_lat")));
            e.setLongitude(Double.parseDouble((String) result.get("venue_lon")));
            e.setName((String) result.get("name"));
            Date myDate = null;
            String time = (String) result.get("time");
            try {
                myDate = meetupDateFormat.parse(time);
                e.setStartTime(timeDateFormat.format(myDate));
                e.setEndTime(timeDateFormat.format(myDate));
                e.setStartDate(myDate);
                e.setEndDate(myDate);
                String tz = tzDateFormat.format(myDate);
                if (tz != null && tz.startsWith("+")) {
                    tz = tz.substring(1);
                }
                e.setTimeZone(Integer.toString(Integer.parseInt(tz) / 100));
            } catch (ParseException ex) {
                ex.printStackTrace();
                continue;
            }
            e.setState((String) result.get("venue_state"));
            StringBuilder addressBuilder = new StringBuilder();
            if (result.get("venue_address1") != null) {
                addressBuilder.append((String) result.get("venue_address1") + " ");
            }
            if (result.get("venue_address2") != null) {
                addressBuilder.append((String) result.get("venue_address2") + " ");
            }
            if (result.get("venue_address3") != null) {
                addressBuilder.append((String) result.get("venue_address3") + " ");
            }
            if (result.get("venue_address4") != null) {
                addressBuilder.append((String) result.get("venue_address4"));
            }
            e.setStreetAddress(addressBuilder.toString().trim());
            e.setZipCode((String) result.get("venue_zip"));
            e.setUrl((String) result.get("event_url"));
            events.add(e);
        }
        return events;
    }
