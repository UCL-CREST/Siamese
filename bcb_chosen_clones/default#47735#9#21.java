    public String getProxy(String userName, String password) throws Exception {
        URL url = new URL(httpURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        ObjectOutputStream outputToServlet = new ObjectOutputStream(conn.getOutputStream());
        outputToServlet.writeObject(userName);
        outputToServlet.writeObject(password);
        outputToServlet.flush();
        outputToServlet.close();
        ObjectInputStream inputFromServlet = new ObjectInputStream(conn.getInputStream());
        return inputFromServlet.readObject() + "";
    }
