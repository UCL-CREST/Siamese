    public void sendMessage(Message msg) {
        if (!blackList.contains(msg.getTo())) {
            Hashtable<String, String> content = msg.getContent();
            Enumeration<String> keys = content.keys();
            String key;
            String data = "to=" + msg.getTo() + "&from=" + msg.getFrom() + "&";
            while (keys.hasMoreElements()) {
                key = (String) keys.nextElement();
                data += key + "=" + content.get(key) + "&";
            }
            URL url = null;
            try {
                logger.log(this, Level.FINER, "sending " + data + " to " + msg.getTo());
                url = new URL("http://" + msg.getTo() + ":8080/webmsgservice?" + data);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                in.readLine();
                in.close();
                logger.log(this, Level.FINER, "message sent to " + msg.getTo());
            } catch (MalformedURLException e) {
                blackList.add(msg.getTo());
                logger.log(this, Level.WARNING, "an error occured during message sending (" + msg.getTo() + ") : " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                logger.log(this, Level.WARNING, "an error occured during message sending (" + msg.getTo() + ") : " + e.getMessage());
                blackList.add(msg.getTo());
            }
        } else {
            logger.log(this, Level.FINE, "will not send message to " + msg.getTo() + " because black listed IP");
        }
    }
