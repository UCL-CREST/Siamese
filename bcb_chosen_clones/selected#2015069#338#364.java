    private void createNodes() {
        try {
            URL url = this.getClass().getResource("NodesFile.txt");
            InputStreamReader inReader = new InputStreamReader(url.openStream());
            BufferedReader inNodes = new BufferedReader(inReader);
            String s;
            while ((s = inNodes.readLine()) != null) {
                String label = inNodes.readLine();
                String fullText = inNodes.readLine();
                String type = inNodes.readLine();
                Node n = new Node(s, type);
                n.label = label;
                n.fullText = fullText;
                node.add(n);
            }
            inNodes.close();
            url = this.getClass().getResource("EdgesFile.txt");
            inReader = new InputStreamReader(url.openStream());
            BufferedReader inEdges = new BufferedReader(inReader);
            while ((s = inEdges.readLine()) != null) edge.add(new Edge(s, inEdges.readLine(), inEdges.readLine(), inEdges.readLine()));
            inEdges.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
