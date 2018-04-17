    public static AddUserResponse napiUserAdd(String user, String pass, String email) throws TimeoutException, InterruptedException, IOException {
        if (user.matches("^[a-zA-Z0-9]{2,20}$") == false) {
            return AddUserResponse.NAPI_ADD_USER_BAD_LOGIN;
        }
        if (pass.equals("")) {
            return AddUserResponse.NAPI_ADD_USER_BAD_PASS;
        }
        if (email.matches("^[a-zA-Z0-9\\-\\_]{1,30}@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+$") == false) {
            return AddUserResponse.NAPI_ADD_USER_BAD_EMAIL;
        }
        URLConnection conn = null;
        ClientHttpRequest httpPost = null;
        InputStreamReader responseStream = null;
        URL url = new URL("http://www.napiprojekt.pl/users_add.php");
        conn = url.openConnection(Global.getProxy());
        httpPost = new ClientHttpRequest(conn);
        httpPost.setParameter("login", user);
        httpPost.setParameter("haslo", pass);
        httpPost.setParameter("mail", email);
        httpPost.setParameter("z_programu", "true");
        responseStream = new InputStreamReader(httpPost.post(), "Cp1250");
        BufferedReader responseReader = new BufferedReader(responseStream);
        String response = responseReader.readLine();
        if (response.indexOf("login już istnieje") != -1) {
            return AddUserResponse.NAPI_ADD_USER_LOGIN_EXISTS;
        }
        if (response.indexOf("na podany e-mail") != -1) {
            return AddUserResponse.NAPI_ADD_USER_EMAIL_EXISTS;
        }
        if (response.indexOf("NPc0") == 0) {
            return AddUserResponse.NAPI_ADD_USER_OK;
        }
        return AddUserResponse.NAPI_ADD_USER_BAD_UNKNOWN;
    }
