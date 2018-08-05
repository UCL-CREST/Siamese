    @SuppressWarnings("finally")
    @Override
    public String read(EnumSensorType sensorType, Map<String, String> stateMap) {
        BufferedReader in = null;
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(getUrl());
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                result.append(str);
            }
        } catch (ConnectException ce) {
            logger.error("MockupStatusCommand excute fail: " + ce.getMessage());
        } catch (Exception e) {
            logger.error("MockupStatusCommand excute fail: " + e.getMessage());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("BufferedReader could not be closed", e);
                }
            }
            return result.toString();
        }
    }
