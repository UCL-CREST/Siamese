    private boolean loadSymbol(QuoteCache quoteCache, Symbol symbol, TradingDate startDate, TradingDate endDate) {
        boolean success = true;
        String URLString = constructURL(symbol, startDate, endDate);
        PreferencesManager.ProxyPreferences proxyPreferences = PreferencesManager.loadProxySettings();
        try {
            URL url;
            url = new URL(URLString);
            InputStreamReader input = new InputStreamReader(url.openStream());
            BufferedReader bufferedInput = new BufferedReader(input);
            String line;
            while ((line = bufferedInput.readLine()) != null) {
                Class cl = null;
                Constructor cnst = null;
                QuoteFilter filter = null;
                try {
                    cl = Class.forName("org.mov.quote." + name + "QuoteFilter");
                    try {
                        cnst = cl.getConstructor(new Class[] { Symbol.class });
                    } catch (SecurityException e2) {
                        e2.printStackTrace();
                    } catch (NoSuchMethodException e2) {
                        e2.printStackTrace();
                    }
                    try {
                        filter = (QuoteFilter) cnst.newInstance(new Object[] { symbol });
                    } catch (IllegalArgumentException e3) {
                        e3.printStackTrace();
                    } catch (InstantiationException e3) {
                        e3.printStackTrace();
                    } catch (IllegalAccessException e3) {
                        e3.printStackTrace();
                    } catch (InvocationTargetException e3) {
                        e3.printStackTrace();
                    }
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
                Quote quote = filter.toQuote(line);
                if (quote != null) quoteCache.load(quote);
            }
            bufferedInput.close();
        } catch (BindException e) {
            DesktopManager.showErrorMessage(Locale.getString("UNABLE_TO_CONNECT_ERROR", e.getMessage()));
            success = false;
        } catch (ConnectException e) {
            DesktopManager.showErrorMessage(Locale.getString("UNABLE_TO_CONNECT_ERROR", e.getMessage()));
            success = false;
        } catch (UnknownHostException e) {
            DesktopManager.showErrorMessage(Locale.getString("UNKNOWN_HOST_ERROR", e.getMessage()));
            success = false;
        } catch (NoRouteToHostException e) {
            DesktopManager.showErrorMessage(Locale.getString("DESTINATION_UNREACHABLE_ERROR", e.getMessage()));
            success = false;
        } catch (MalformedURLException e) {
            DesktopManager.showErrorMessage(Locale.getString("INVALID_PROXY_ERROR", proxyPreferences.host, proxyPreferences.port));
            success = false;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            DesktopManager.showErrorMessage(Locale.getString("ERROR_DOWNLOADING_QUOTES"));
            success = false;
        }
        return success;
    }
