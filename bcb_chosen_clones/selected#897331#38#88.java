    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        HttpGet request = new HttpGet(SERVICE_URI + "/json/getroutes/1");
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String theString = new String("");
        try {
            HttpResponse response = httpClient.execute(request);
            HttpEntity responseEntity = response.getEntity();
            InputStream stream = responseEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            Vector<String> vectorOfStrings = new Vector<String>();
            String tempString = new String();
            String tempStringAgent = new String();
            String tempStringClient = new String();
            String tempStringRoute = new String();
            String tempStringZone = new String();
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            stream.close();
            theString = builder.toString();
            JSONObject json = new JSONObject(theString);
            Log.i("_GetClient_", "<jsonobject>\n" + json.toString() + "\n</jsonobject>");
            this.dm = new DataManipulator(this);
            JSONArray nameArray = json.getJSONArray("GetRoutesByAgentResult");
            for (int i = 0; i < nameArray.length(); i++) {
                tempStringAgent = nameArray.getJSONObject(i).getString("Agent");
                tempStringClient = nameArray.getJSONObject(i).getString("Client");
                tempStringRoute = nameArray.getJSONObject(i).getString("Route");
                tempStringZone = nameArray.getJSONObject(i).getString("Zone");
                Log.i("_GetClient_", "<Agent" + i + ">" + tempStringAgent + "</Agent" + i + ">\n");
                Log.i("_GetClient_", "<Client" + i + ">" + tempStringClient + "</Client" + i + ">\n");
                Log.i("_GetClient_", "<Route" + i + ">" + tempStringRoute + "</Route" + i + ">\n");
                Log.i("_GetClient_", "<Zone" + i + ">" + tempStringZone + "</Zone" + i + ">\n");
                tempString = nameArray.getJSONObject(i).getString("Client") + "\n" + nameArray.getJSONObject(i).getString("Route") + "\n" + nameArray.getJSONObject(i).getString("Zone");
                vectorOfStrings.add(new String(tempString));
            }
            int orderCount = vectorOfStrings.size();
            String[] orderTimeStamps = new String[orderCount];
            vectorOfStrings.copyInto(orderTimeStamps);
            setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, orderTimeStamps));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
