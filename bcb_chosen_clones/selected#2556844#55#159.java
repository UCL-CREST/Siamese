    public GraphLayout layoutNodes(JGraph graph) {
        LinkedHashSet<Component> nodes = graph.getNodes();
        if (nodes.size() <= 0) return null;
        Component[] nodeArray = nodes.toArray(new Component[0]);
        boolean[][] adj = new boolean[nodeArray.length][nodeArray.length];
        for (int i = 0; i < nodeArray.length; i++) {
            adj[i][i] = false;
            for (int j = i + 1; j < nodeArray.length; j++) {
                adj[i][j] = graph.isConnected(nodeArray[i], nodeArray[j]);
                adj[j][i] = adj[i][j];
            }
        }
        double d[] = pageRank(adj);
        Hashtable<RankedGraphNode, RankedGraphNode> parents = new Hashtable<RankedGraphNode, RankedGraphNode>();
        PriorityQueue<RankedGraphNode> frontier = new PriorityQueue<RankedGraphNode>();
        LinkedHashSet<RankedGraphNode> remainingNodes = new LinkedHashSet<RankedGraphNode>();
        RankedGraphNode max = null;
        for (int i = 0; i < nodeArray.length; i++) {
            RankedGraphNode rgn = new RankedGraphNode(nodeArray[i], d[i], i);
            remainingNodes.add(rgn);
            if (max == null || d[i] > max.getPageRank()) max = rgn;
        }
        remainingNodes.remove(max);
        frontier.add(max);
        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        Rectangle2D.Double placements[] = new Rectangle2D.Double[d.length];
        Arrays.fill(placements, null);
        int NODE_INTERSECTION_PENALTY = 5000;
        while (!frontier.isEmpty()) {
            RankedGraphNode n = frontier.poll();
            RankedGraphNode parent = parents.get(n);
            int idx = n.getIndex();
            Dimension s = n.getComponent().getPreferredSize();
            if (parent == null) {
                placements[idx] = new Rectangle2D.Double(0.0 - s.getWidth() / 2.0, 0.0 - s.getHeight() / 2.0, s.getWidth(), s.getHeight());
            } else {
                int parentIdx = parent.getIndex();
                double minCost = Double.MAX_VALUE;
                Rectangle2D.Double bestPlacement = null;
                for (double distance = (double) MINIMUM_NODE_SEPARATION; distance < (double) (MINIMUM_NODE_SEPARATION * 2); distance += 5.0) {
                    for (double angle = -1.0 * Math.PI; angle < Math.PI; angle += Math.PI / 8.0) {
                        double centerX = placements[parentIdx].getX() + placements[parentIdx].getWidth() / 2.0 + distance * Math.cos(angle);
                        double centerY = placements[parentIdx].getY() + placements[parentIdx].getHeight() / 2.0 + distance * Math.sin(angle);
                        double edgeCost = 0;
                        int numEdges = 0;
                        double nonEdgeCost = 0;
                        int numNonEdges = 0;
                        Rectangle2D.Double loc = new Rectangle2D.Double(centerX - s.getWidth() / 2.0, centerY - s.getHeight() / 2.0, s.getWidth(), s.getHeight());
                        double cost = 0;
                        for (int i = 0; i < placements.length; i++) {
                            if (i == idx || placements[i] == null) continue;
                            if (loc.intersects(placements[i])) cost += NODE_INTERSECTION_PENALTY;
                            double neighborCenterX = placements[i].getX() + placements[i].getWidth() / 2.0;
                            double neighborCenterY = placements[i].getY() + placements[i].getHeight() / 2.0;
                            double xDiff = neighborCenterX - centerX;
                            double yDiff = neighborCenterY - centerY;
                            double h = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
                            if (graph.isConnected(n.getComponent(), nodeArray[i]) && h >= MINIMUM_NODE_SEPARATION) {
                                edgeCost += h;
                                numEdges++;
                            } else {
                                if (h < MINIMUM_NODE_SEPARATION) h = MINIMUM_NODE_SEPARATION;
                                nonEdgeCost += h;
                                numNonEdges++;
                            }
                        }
                        double avgEdgeCost = 0;
                        double avgNonEdgeCost = 0;
                        if (numNonEdges > 0) avgNonEdgeCost = nonEdgeCost / (double) numNonEdges; else avgNonEdgeCost = 0;
                        if (edgeCost == 0) {
                            cost += 0 - avgNonEdgeCost;
                        } else {
                            avgEdgeCost = edgeCost / (double) numEdges;
                            cost += avgEdgeCost - avgNonEdgeCost;
                        }
                        if (bestPlacement == null || cost < minCost) {
                            minCost = cost;
                            bestPlacement = loc;
                        }
                    }
                }
                placements[idx] = bestPlacement;
                if (bestPlacement.getX() < minX) minX = bestPlacement.getX();
                if (bestPlacement.getX() + bestPlacement.getWidth() > maxX) maxX = bestPlacement.getX() + bestPlacement.getWidth();
                if (bestPlacement.getY() < minY) minY = bestPlacement.getY();
                if (bestPlacement.getY() + bestPlacement.getHeight() > maxY) maxY = bestPlacement.getY() + bestPlacement.getHeight();
            }
            for (Iterator<RankedGraphNode> iter = remainingNodes.iterator(); iter.hasNext(); ) {
                RankedGraphNode rgn = iter.next();
                if (graph.isConnected(rgn.getComponent(), n.getComponent())) {
                    iter.remove();
                    frontier.add(rgn);
                    parents.put(rgn, n);
                }
            }
        }
        Dimension size = new Dimension((int) (maxX - minX + 0.5), (int) (maxY - minY + 0.5));
        for (int i = 0; i < nodeArray.length; i++) {
            if (placements[i] != null) nodeArray[i].setBounds((int) (placements[i].getX() - minX + 0.5), (int) (placements[i].getY() - minY + 0.5), (int) (placements[i].getWidth()), (int) (placements[i].getHeight()));
        }
        return new GraphLayout(size, nodeArray);
    }
