    @Override
    public void loadData() {
        try {
            String url = "http://ichart.finance.yahoo.com/table.csv?s=" + symbol + "&a=00&b=2&c=1962&d=11&e=11&f=2099&g=d&ignore=.csv";
            URLConnection conn = (new URL(url)).openConnection();
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            in.readLine();
            String str = "";
            while ((str = in.readLine()) != null) {
                final String[] split = str.split(",");
                final String date = split[0];
                final double open = Double.parseDouble(split[1]);
                final double high = Double.parseDouble(split[2]);
                final double low = Double.parseDouble(split[3]);
                final double close = Double.parseDouble(split[4]);
                final int volume = Integer.parseInt(split[5]);
                final double adjClose = Double.parseDouble(split[6]);
                final HistoricalPrice price = new HistoricalPrice(date, open, high, low, close, volume, adjClose);
                historicalPrices.add(price);
            }
            in.close();
            url = "http://ichart.finance.yahoo.com/table.csv?s=" + symbol + "&a=00&b=2&c=1962&d=11&e=17&f=2008&g=v&ignore=.csv";
            conn = (new URL(url)).openConnection();
            conn.connect();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            in.readLine();
            str = "";
            while ((str = in.readLine()) != null) {
                final String[] split = str.split(",");
                final String date = split[0];
                final double amount = Double.parseDouble(split[1]);
                final Dividend dividend = new Dividend(date, amount);
                dividends.add(dividend);
            }
            in.close();
        } catch (final IOException ioe) {
            logger.error("Error parsing historical prices for " + getSymbol(), ioe);
        }
    }
