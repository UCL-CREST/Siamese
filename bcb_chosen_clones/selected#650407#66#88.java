    private EventSeries<PhotoEvent> loadIncomingEvents(long reportID) {
        EventSeries<PhotoEvent> events = new EventSeries<PhotoEvent>();
        try {
            URL url = new URL(SERVER_URL + XML_PATH + "reports.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = reader.readLine()) != null) {
                String[] values = str.split(",");
                if (values.length == 2) {
                    long id = Long.parseLong(values[0]);
                    if (id == reportID) {
                        long time = Long.parseLong(values[1]);
                        events.addEvent(new PhotoEvent(time));
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return events;
    }
