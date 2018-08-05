    public void testExecute() throws Exception {
        LocalWorker worker = new JTidyWorker();
        URL url = new URL("http://www.nature.com/index.html");
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String str;
        StringBuffer sb = new StringBuffer();
        while ((str = in.readLine()) != null) {
            sb.append(str);
            sb.append(LINE_ENDING);
        }
        in.close();
        Map inputMap = new HashMap();
        DataThingAdapter inAdapter = new DataThingAdapter(inputMap);
        inAdapter.putString("inputHtml", sb.toString());
        Map outputMap = worker.execute(inputMap);
        DataThingAdapter outAdapter = new DataThingAdapter(outputMap);
        assertNotNull("The outputMap was null", outputMap);
        String results = outAdapter.getString("results");
        assertFalse("The results were empty", results.equals(""));
        assertNotNull("The results were null", results);
    }
