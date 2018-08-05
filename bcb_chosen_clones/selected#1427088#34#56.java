    private String createVisadFile(String fileName) throws FileNotFoundException, IOException {
        ArrayList<String> columnNames = new ArrayList<String>();
        String visadFile = fileName + ".visad";
        BufferedReader buf = new BufferedReader(new FileReader(fileName));
        String firstLine = buf.readLine().replace('"', ' ');
        StringTokenizer st = new StringTokenizer(firstLine, ",");
        while (st.hasMoreTokens()) columnNames.add(st.nextToken());
        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append("(").append(columnNames.get(0)).append("->(");
        for (int i = 1; i < columnNames.size(); i++) {
            headerBuilder.append(columnNames.get(i));
            if (i < columnNames.size() - 1) headerBuilder.append(",");
        }
        headerBuilder.append("))");
        BufferedWriter out = new BufferedWriter(new FileWriter(visadFile));
        out.write(headerBuilder.toString() + "\n");
        out.write(firstLine + "\n");
        String line;
        while ((line = buf.readLine()) != null) out.write(line + "\n");
        buf.close();
        out.close();
        return visadFile;
    }
