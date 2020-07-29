    public void testMySqlCall() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec("ping localhost");
            DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(process.getInputStream()));
            for (String outptFromPrc = inputStream.readLine(); (outptFromPrc = inputStream.readLine()) != null; ) {
                System.out.println(outptFromPrc);
            }
            BufferedReader errorInputStream = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            for (String outptFromPrc = errorInputStream.readLine(); (outptFromPrc = errorInputStream.readLine()) != null; ) {
                System.out.println(outptFromPrc);
            }
            System.out.println(process.exitValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
