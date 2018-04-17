    public static ContextInfo login(Context pContext, String pUsername, String pPwd, String pDeviceid) {
        HttpClient lClient = new DefaultHttpClient();
        StringBuilder lBuilder = new StringBuilder();
        ContextInfo lContextInfo = null;
        HttpPost lHttpPost = new HttpPost(new StringBuilder().append("http://").append(LoginActivity.mIpAddress.getText().toString()).append("/ZJWHServiceTest/GIS_Duty.asmx/PDALoginCheck").toString());
        List<NameValuePair> lNameValuePairs = new ArrayList<NameValuePair>(2);
        lNameValuePairs.add(new BasicNameValuePair("username", pUsername));
        lNameValuePairs.add(new BasicNameValuePair("password", pPwd));
        lNameValuePairs.add(new BasicNameValuePair("deviceid", pDeviceid));
        try {
            lHttpPost.setEntity(new UrlEncodedFormEntity(lNameValuePairs));
            HttpResponse lResponse = lClient.execute(lHttpPost);
            BufferedReader lHeader = new BufferedReader(new InputStreamReader(lResponse.getEntity().getContent()));
            for (String s = lHeader.readLine(); s != null; s = lHeader.readLine()) {
                lBuilder.append(s);
            }
            String lResult = lBuilder.toString();
            lResult = DataParseUtil.handleResponse(lResult);
            lContextInfo = LoginParseUtil.onlineParse(lResult);
            lContextInfo.setDeviceid(pDeviceid);
            if (0 == lContextInfo.getLoginFlag()) {
                lContextInfo.setLoginFlag(0);
            } else if (1 == lContextInfo.getLoginFlag()) {
                lContextInfo.setLoginFlag(1);
                updateUserInfo(pContext, lContextInfo);
            } else if (2 == lContextInfo.getLoginFlag()) {
                lContextInfo.setLoginFlag(2);
            } else if (3 == lContextInfo.getLoginFlag()) {
                lContextInfo.setLoginFlag(3);
            }
        } catch (Exception e) {
            return lContextInfo;
        }
        return lContextInfo;
    }
