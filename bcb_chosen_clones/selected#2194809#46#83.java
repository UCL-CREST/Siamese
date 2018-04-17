    @Override
    public void onClick(View view) {
        String ftpHostname = getFtpHostname();
        String ftpUsername = getFtpUsername();
        String ftpPassword = getFtpPassword();
        int ftpPort = getFtpPort();
        String dialogText = getString(R.string.testingFTPConnectionDialogHeaderText) + " " + ftpHostname;
        ProgressDialog dialog = ProgressDialog.show(this, "", dialogText, true);
        String result = "NOTHING??";
        Log.i(TAG, "Test attempt login to " + ftpHostname + " as " + ftpUsername);
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpHostname, ftpPort);
            ftpClient.login(ftpUsername, ftpPassword);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                result = getString(R.string.testFTPConnectionDeniedString);
                Log.w(TAG, result);
            } else {
                result = getString(R.string.testFTPSuccessString);
                Log.i(TAG, result);
            }
        } catch (Exception ex) {
            result = getString(R.string.testFTPFailString) + ex;
            Log.w(TAG, result);
        } finally {
            try {
                dialog.dismiss();
                ftpClient.disconnect();
            } catch (Exception e) {
            }
        }
        Context context = getApplicationContext();
        CharSequence text = result;
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
