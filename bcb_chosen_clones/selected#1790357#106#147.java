    public void run() {
        BufferedReader reader = null;
        log = "Downloading... " + name;
        setChanged();
        notifyObservers();
        try {
            Date marker = to;
            int previousSize = 0;
            list.clear();
            do {
                previousSize = list.size();
                URL url = new URL(createLink(from, marker));
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    try {
                        IQuoteHistorical quote = parse(line + ",");
                        if (quote != null && !list.contains(quote)) list.add(quote); else System.err.println(line);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (list.size() > 0) marker = list.get(list.size() - 1).getData();
            } while (marker.after(from) && previousSize != list.size());
            log = "download Completed!";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            log = e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
            log = e.getMessage();
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                log = e.getMessage();
            }
        }
        setChanged();
        notifyObservers();
    }
