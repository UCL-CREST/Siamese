    public WorldReader(String worldFile, WIC WI) {
        String storeWorld = "";
        String temp = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(worldFile));
            while ((temp = in.readLine()) != null) {
                storeWorld = storeWorld + temp;
            }
        } catch (IOException e) {
        }
        char[] worldChar = storeWorld.toCharArray();
        int worldCounter = 0;
        while (worldCounter < storeWorld.length()) {
            char c = worldChar[worldCounter];
            if (c == '<') {
                String[] inputs = new String[3];
                int start = worldCounter + 1;
                int end = worldCounter;
                while (c != '>') {
                    end++;
                    c = worldChar[end];
                }
                String parseWorld = storeWorld.substring(start, end);
                StringTokenizer st = new StringTokenizer(parseWorld);
                String keyword = st.nextToken();
                int inputCounter = 0;
                while (st.hasMoreTokens()) {
                    inputs[inputCounter] = st.nextToken();
                    inputCounter++;
                }
                newWorld = findCommand(keyword, inputs, newWorld, WI);
                worldCounter = end + 1;
            } else {
                worldCounter++;
            }
        }
    }
