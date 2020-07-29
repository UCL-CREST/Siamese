    private Object[] retrieveSecondURL(URL url, RSLink link) {
        link.setStatus(RSLink.STATUS_WAITING);
        Object[] result = new Object[2];
        HttpURLConnection httpConn = null;
        BufferedReader inr = null;
        DataOutputStream outs = null;
        Pattern mirrorLinePattern = Pattern.compile("'<input.+checked.+type=\"radio\".+name=\"mirror\".+\\\\'.+\\\\'");
        Pattern mirrorUrlPattern = Pattern.compile("\\\\'.+\\\\'");
        Pattern counterPattern = Pattern.compile("var c=[0-9]+;");
        Pattern counterIntPattern = Pattern.compile("[0-9]+");
        try {
            String line = null;
            String urlLine = null;
            Integer counter = null;
            String postData = URLEncoder.encode("dl.start", "UTF-8") + "=" + URLEncoder.encode("Free", "UTF-8");
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConn.setRequestProperty("Content-Length", "" + Integer.toString(postData.getBytes().length));
            httpConn.setRequestProperty("Content-Language", "en-US");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            outs = new DataOutputStream(httpConn.getOutputStream());
            outs.writeBytes(postData);
            outs.flush();
            inr = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            Matcher matcher = null;
            while ((line = inr.readLine()) != null) {
                matcher = mirrorLinePattern.matcher(line);
                if (matcher.find()) {
                    matcher = mirrorUrlPattern.matcher(line);
                    if (matcher.find()) {
                        urlLine = matcher.group().substring(2, matcher.group().length() - 2);
                        result[0] = new URL(urlLine);
                    }
                }
                matcher = counterPattern.matcher(line);
                if (matcher.find()) {
                    matcher = counterIntPattern.matcher(line);
                    if (matcher.find()) {
                        counter = new Integer(matcher.group());
                        result[1] = counter;
                    }
                }
            }
        } catch (IOException ex) {
            log("I/O Exception!");
        } finally {
            try {
                if (outs != null) outs.close();
                if (inr != null) inr.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Can not close some connections:\n" + ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            if (httpConn != null) httpConn.disconnect();
            link.setStatus(RSLink.STATUS_NOTHING);
            return result;
        }
    }
