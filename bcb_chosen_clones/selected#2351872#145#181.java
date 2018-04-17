    private void executeRequest(OperationContext context) throws java.lang.Throwable {
        long t1 = System.currentTimeMillis();
        DirectoryParams params = context.getRequestOptions().getDirectoryOptions();
        try {
            String srvCfg = context.getRequestContext().getApplicationConfiguration().getCatalogConfiguration().getParameters().getValue("openls.directory");
            HashMap<String, String> poiProperties = params.getPoiProperties();
            Set<String> keys = poiProperties.keySet();
            Iterator<String> iter = keys.iterator();
            StringBuffer filter = new StringBuffer();
            while (iter.hasNext()) {
                String key = iter.next();
                QueryFilter queryFilter = new QueryFilter(key, poiProperties.get(key));
                filter.append(makePOIRequest(queryFilter));
            }
            String sUrl = srvCfg + "/query?" + filter.toString();
            LOGGER.info("REQUEST=\n" + sUrl);
            URL url = new URL(sUrl);
            URLConnection conn = url.openConnection();
            String line = "";
            String sResponse = "";
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader rd = new BufferedReader(isr);
            while ((line = rd.readLine()) != null) {
                sResponse += line;
            }
            rd.close();
            url = null;
            parsePOIResponse(sResponse, params);
        } catch (Exception p_e) {
            LOGGER.severe("Throwing exception" + p_e.getMessage());
            throw p_e;
        } finally {
            long t2 = System.currentTimeMillis();
            LOGGER.info("PERFORMANCE: " + (t2 - t1) + " ms spent performing service");
        }
    }
