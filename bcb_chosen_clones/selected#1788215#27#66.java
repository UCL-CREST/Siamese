    public ActionResponse executeAction(ActionRequest request) throws Exception {
        BufferedReader in = null;
        try {
            CurrencyEntityManager em = new CurrencyEntityManager();
            String id = (String) request.getProperty("ID");
            CurrencyMonitor cm = getCurrencyMonitor(em, Long.valueOf(id));
            String code = cm.getCode();
            if (code == null || code.length() == 0) code = DEFAULT_SYMBOL;
            String tmp = URL.replace("@", code);
            ActionResponse resp = new ActionResponse();
            URL url = new URL(tmp);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int status = conn.getResponseCode();
            if (status == 200) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder value = new StringBuilder();
                while (true) {
                    String line = in.readLine();
                    if (line == null) break;
                    value.append(line);
                }
                cm.setLastUpdateValue(new BigDecimal(value.toString()));
                cm.setLastUpdateTs(new Date());
                em.updateCurrencyMonitor(cm);
                resp.addResult("CURRENCYMONITOR", cm);
            } else {
                resp.setErrorCode(ActionResponse.GENERAL_ERROR);
                resp.setErrorMessage("HTTP Error [" + status + "]");
            }
            return resp;
        } catch (Exception e) {
            String st = MiscUtils.stackTrace2String(e);
            logger.error(st);
            throw e;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
