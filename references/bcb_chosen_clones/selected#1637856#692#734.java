    private void createNodes() {
        try {
            URL url = this.getClass().getResource(this.nodeFileName);
            InputStreamReader inReader = new InputStreamReader(url.openStream());
            BufferedReader inNodes = new BufferedReader(inReader);

            // BufferedReader inNodes = new BufferedReader(new
            // FileReader("NodesFile.txt"));
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

            url = this.getClass().getResource(this.edgeFileName);
            inReader = new InputStreamReader(url.openStream());
            BufferedReader inEdges = new BufferedReader(inReader);
            // BufferedReader inEdges = new BufferedReader(new
            // FileReader("EdgesFile.txt"));
            while ((s = inEdges.readLine()) != null)
                edge.add(new Edge(s, inEdges.readLine(), inEdges.readLine(),
                        inEdges.readLine()));
            inEdges.close();
        } catch (FileNotFoundException e) {
            // TODO �Զ���� catch ��
            e.printStackTrace();
        } catch (IOException e) {
            // TODO �Զ���� catch ��
            e.printStackTrace();
        }
        /*
         * for(Myparser.Nd x:FreeConnectTest.pNd){ Node n = new Node(x.id,
         * x.type); n.label = x.label; node.add(n); } for(Myparser.Ed
         * x:FreeConnectTest.pEd) edge.add(new Edge(x.id, x.source.id,
         * x.target.id));
         */
    }
