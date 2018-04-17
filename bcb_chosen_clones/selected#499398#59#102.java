    public static boolean reportException(Throwable ex, HashMap<String, String> suppl) {
        if (Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_CRASH_REPORTING)) {
            logger.debug("Report exception to devs...");
            String data = "reportType=exception&" + "message=" + ex.getMessage();
            data += "&build=" + Platform.getBundle("de.uni_mannheim.swt.codeconjurer").getHeaders().get("Bundle-Version");
            int ln = 0;
            for (StackTraceElement el : ex.getStackTrace()) {
                data += "&st_line_" + ++ln + "=" + el.getClassName() + "#" + el.getMethodName() + "<" + el.getLineNumber() + ">";
            }
            data += "&lines=" + ln;
            data += "&Suppl-Description=" + ex.toString();
            data += "&Suppl-Server=" + Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_SERVER);
            data += "&Suppl-User=" + Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_USERNAME);
            if (suppl != null) {
                for (String key : suppl.keySet()) {
                    data += "&Suppl-" + key + "=" + suppl.get(key);
                }
            }
            try {
                URL url = new URL("http://www.merobase.com:7777/org.code_conjurer.udc/CrashReport");
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(data);
                writer.flush();
                StringBuffer answer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    answer.append(line + "\r\n");
                }
                writer.close();
                reader.close();
                logger.debug(answer.toString());
            } catch (Exception e) {
                logger.debug("Could not report exception");
                return false;
            }
            return true;
        } else {
            logger.debug("Reporting not wished!");
            return false;
        }
    }
