    public static ArrayList<Quote> fetchAllQuotes(String symbol, Date from, Date to) {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(from);
            String monthFrom = (new Integer(calendar.get(GregorianCalendar.MONTH))).toString();
            String dayFrom = (new Integer(calendar.get(GregorianCalendar.DAY_OF_MONTH))).toString();
            String yearFrom = (new Integer(calendar.get(GregorianCalendar.YEAR))).toString();
            calendar.setTime(to);
            String monthTo = (new Integer(calendar.get(GregorianCalendar.MONTH))).toString();
            String dayTo = (new Integer(calendar.get(GregorianCalendar.DAY_OF_MONTH))).toString();
            String yearTo = (new Integer(calendar.get(GregorianCalendar.YEAR))).toString();
            URL url = new URL("http://ichart.finance.yahoo.com/table.csv?s=" + symbol + "&a=" + monthFrom + "&b=" + dayFrom + "&c=" + yearFrom + "&d=" + monthTo + "&e=" + dayTo + "&f=" + yearTo + "&g=d&ignore=.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            ArrayList<Quote> result = new ArrayList<Quote>();
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                String date = values[0];
                Date dateQuote = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                double open = Double.valueOf(values[1]);
                double high = Double.valueOf(values[2]);
                double low = Double.valueOf(values[3]);
                double close = Double.valueOf(values[4]);
                long volume = Long.valueOf(values[5]);
                double adjClose = Double.valueOf(values[6]);
                Quote q = new Quote(dateQuote, open, high, low, close, volume, adjClose);
                result.add(q);
            }
            reader.close();
            Collections.reverse(result);
            return result;
        } catch (MalformedURLException e) {
            System.out.println("URL malformee");
        } catch (IOException e) {
            System.out.println("Donnees illisibles");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
