    public Heap(BufferedReader reader) throws Exception {
        reader.readLine();
        int hLength = lastUsedIndex = Integer.parseInt(reader.readLine().trim());
        heap = new int[hLength];
        comments = new String[hLength];
        reader.readLine();
        for (int i = 0; i < hLength; i++) {
            String nextLine = "";
            while (nextLine.length() == 0) nextLine = reader.readLine().trim();
            StringTokenizer strtok = new StringTokenizer(nextLine);
            if (strtok.countTokens() > 0) {
                strtok.nextToken();
                heap[i] = Integer.parseInt(strtok.nextToken());
                comments[i] = "";
                while (strtok.hasMoreTokens()) comments[i] = comments[i] + strtok.nextToken() + " ";
            }
        }
    }
