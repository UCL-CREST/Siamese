    public GraphLayout layoutNodes(JGraph graph) {
        LinkedHashSet<Component> nodes = graph.getNodes();
        if (nodes.size() <= 0) return null;
        Component[] nodeArray = nodes.toArray(new Component[0]);
        boolean[][] adj = new boolean[nodeArray.length][nodeArray.length];
        int distance[][] = new int[nodeArray.length][nodeArray.length];
        for (int i = 0; i < nodeArray.length; i++) {
            adj[i][i] = false;
            distance[i][i] = 0;
            for (int j = i + 1; j < nodeArray.length; j++) {
                adj[i][j] = graph.isConnected(nodeArray[i], nodeArray[j]);
                adj[j][i] = adj[i][j];
                distance[i][j] = adj[i][j] ? 1 : nodeArray.length + 1;
                distance[j][i] = distance[i][j];
            }
        }
        for (int k = 0; k < nodeArray.length; k++) for (int i = 0; i < nodeArray.length; i++) for (int j = 0; j < nodeArray.length; j++) distance[i][j] = Math.min(distance[i][j], distance[i][k] + distance[k][j]);
        int ordering[] = new int[nodeArray.length];
        {
            Node centrality[] = new Node[nodeArray.length];
            for (int i = 0; i < nodeArray.length; i++) {
                centrality[i] = new Node(i, 0);
                for (int j = 0; j < nodeArray.length; j++) centrality[i].ordering += distance[i][j];
            }
            Arrays.sort(centrality);
            System.out.print("Ordering: ");
            for (int i = 0; i < nodeArray.length; i++) {
                if (i > 0) System.out.print(", ");
                System.out.print("" + centrality[i].idx);
                ordering[centrality[i].idx] = i;
            }
            System.out.println("");
        }
        double velocityX[] = new double[nodeArray.length];
        double velocityY[] = new double[nodeArray.length];
        double posX[] = new double[nodeArray.length];
        double posY[] = new double[nodeArray.length];
        Random rand = new Random();
        int minX = Integer.MAX_VALUE;
        int maxX = 0;
        int minY = Integer.MAX_VALUE;
        int maxY = 0;
        for (int idx = ordering.length - 1; idx >= 0; idx--) {
            int i = ordering[idx];
            velocityX[i] = 0.0;
            velocityY[i] = 0.0;
            HashSet<Integer> connected = new HashSet<Integer>();
            for (int idx2 = ordering.length - 1; idx2 > idx; idx2--) {
                int j = ordering[idx2];
                if (distance[i][j] == 0 || distance[i][j] >= nodeArray.length) continue;
                connected.add(j);
            }
            boolean placeRandomly = false;
            if (connected.isEmpty()) {
                placeRandomly = true;
            } else if (connected.size() == 1) {
                int j = 0;
                for (Integer j2 : connected) j = j2;
                posX[i] = posX[j];
                posY[i] = posY[j] + EDGE_LENGTH * (double) distance[i][j];
            } else {
                double iX = Double.MAX_VALUE;
                double aX = Double.MIN_VALUE;
                double iY = Double.MAX_VALUE;
                double aY = Double.MIN_VALUE;
                int distanceSum = 0;
                for (Integer j : connected) {
                    if (posX[j] < iX) iX = posX[j];
                    if (posX[j] > aX) aX = posX[j];
                    if (posY[j] < iY) iY = posY[j];
                    if (posY[j] > aY) aY = posY[j];
                    distanceSum += distance[i][j];
                }
                double xTotal = 0.0;
                double yTotal = 0.0;
                double windowX = aX - iX;
                double windowY = aY - iY;
                for (Integer j : connected) {
                    double weight = (double) distance[i][j] / (double) distanceSum;
                    if (windowX != 0.0) xTotal += (posX[j] - iX) / windowX * weight;
                    if (windowY != 0.0) yTotal += (posY[j] - iY) / windowY * weight;
                }
                if (windowX == 0.0) posX[i] = iX; else posX[i] = iX + xTotal * windowX;
                if (windowY == 0.0) posY[i] = iY; else posY[i] = iY + yTotal * windowY;
            }
            do {
                if (placeRandomly) {
                    posX[i] = (double) rand.nextInt(nodeArray.length * 10 + 1);
                    posY[i] = (double) rand.nextInt(nodeArray.length * 10 + 1);
                    placeRandomly = false;
                }
                for (int idx2 = ordering.length - 1; idx2 > idx; idx2--) {
                    int j = ordering[idx2];
                    if ((int) (posX[j] + 0.5) == (int) (posX[i] + 0.5) && (int) posY[j] == (int) (posY[i] + 0.5)) {
                        placeRandomly = true;
                        break;
                    }
                }
            } while (placeRandomly);
            System.out.println(Integer.toString(i) + " = (" + posX[i] + ", " + posY[i] + ")");
        }
        double kineticEnergy;
        do {
            kineticEnergy = 0;
            for (int idx = 0; idx < nodeArray.length; idx++) {
                int i = ordering[idx];
                double forceX = 0;
                double forceY = 0;
                for (int j = 0; j < nodeArray.length; j++) {
                    if (distance[i][j] == 0) continue;
                    double diffX = posX[j] - posX[i];
                    double diffY = posY[j] - posY[i];
                    double currentDistance = Math.sqrt(diffX * diffX + diffY * diffY);
                    double totalForce;
                    if (distance[i][j] < nodeArray.length) totalForce = SPRING_CONSTANT * (currentDistance - EDGE_LENGTH * (double) distance[i][j]); else totalForce = -EDGE_LENGTH / currentDistance;
                    if (diffX == 0.0) forceY += totalForce; else if (diffY == 0.0) forceX += totalForce; else {
                        double fy = (totalForce * diffY) / currentDistance;
                        forceY += fy;
                        double fx = Math.sqrt(totalForce * totalForce - fy * fy);
                        if (totalForce < 0.0 && diffX > 0.0 || totalForce > 0.0 && diffX < 0.0) fx *= -1.0;
                        forceX += fx;
                    }
                }
                velocityX[i] = (velocityX[i] + TIME_STEP * forceX) * DAMPING_FACTOR;
                velocityY[i] = (velocityY[i] + TIME_STEP * forceY) * DAMPING_FACTOR;
                posX[i] = posX[i] + TIME_STEP * velocityX[i];
                posY[i] = posY[i] + TIME_STEP * velocityY[i];
                if (posX[i] + 0.5 < minX) minX = (int) (posX[i] + 0.5);
                if (posX[i] + 0.5 > maxX) maxX = (int) (posX[i] + 0.5);
                if (posY[i] + 0.5 < minY) minY = (int) (posY[i] + 0.5);
                if (posY[i] + 0.5 > maxY) maxY = (int) (posY[i] + 0.5);
                double v = Math.sqrt(velocityX[i] * velocityX[i] + velocityY[i] * velocityY[i]);
                kineticEnergy += 0.5 * v * v;
            }
            System.out.println("KE: " + kineticEnergy);
        } while (kineticEnergy / (double) nodeArray.length > 0.5);
        Dimension size = new Dimension((int) (maxX - minX + 0.5), (int) (maxY - minY + 0.5));
        for (int i = 0; i < nodeArray.length; i++) nodeArray[i].setBounds((int) (posX[i] - minX + 0.5), (int) (posY[i] - minY + 0.5), (int) (nodeArray[i].getPreferredSize().getWidth()), (int) (nodeArray[i].getPreferredSize().getHeight()));
        return new GraphLayout(size, nodeArray);
    }
