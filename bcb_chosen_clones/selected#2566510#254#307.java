    protected void initializeShortestPaths(int gridSize, Point[] vertices, ArrayList<Line2D.Double> walls, float[][] pathLengths, float[][] edgeCosts, ArrayList<Point>[][] shortestPaths) {
        for (int i = 0; i < edgeCosts.length; i++) {
            int x1 = (int) vertices[i].getX();
            int y1 = (int) vertices[i].getY();
            Point startVertex = vertices[i];
            pathLengths[i][i] = 0;
            edgeCosts[i][i] = 0;
            shortestPaths[i][i] = new ArrayList<Point>();
            shortestPaths[i][i].add(startVertex);
            for (int j = i + 1; j < edgeCosts.length; j++) {
                int x2 = (int) vertices[j].getX();
                int y2 = (int) vertices[j].getY();
                int xDiff = Math.abs(x2 - x1);
                int yDiff = Math.abs(y2 - y1);
                if ((xDiff == gridSize && (yDiff == 0 || yDiff == gridSize)) || (xDiff == 0 && yDiff == gridSize)) {
                    Line2D.Double edgeLine = new Line2D.Double(x1, y1, x2, y2);
                    boolean edgeClear = true;
                    for (Line2D.Double wall : walls) {
                        if (wall.intersectsLine(edgeLine)) {
                            edgeClear = false;
                            break;
                        }
                    }
                    if (edgeClear) {
                        Point endVertex = vertices[j];
                        pathLengths[i][j] = calculateEuclideanDistance(x1, y1, x2, y2);
                        pathLengths[j][i] = pathLengths[i][j];
                        edgeCosts[i][j] = pathLengths[i][j];
                        edgeCosts[j][i] = pathLengths[i][j];
                        shortestPaths[i][j] = new ArrayList<Point>();
                        shortestPaths[i][j].add(startVertex);
                        shortestPaths[i][j].add(endVertex);
                        shortestPaths[j][i] = new ArrayList<Point>();
                        shortestPaths[j][i].add(endVertex);
                        shortestPaths[j][i].add(startVertex);
                    } else {
                        pathLengths[i][j] = Float.POSITIVE_INFINITY;
                        pathLengths[j][i] = Float.POSITIVE_INFINITY;
                        edgeCosts[i][j] = Float.POSITIVE_INFINITY;
                        edgeCosts[j][i] = Float.POSITIVE_INFINITY;
                        shortestPaths[i][j] = new ArrayList<Point>();
                        shortestPaths[j][i] = new ArrayList<Point>();
                    }
                } else {
                    pathLengths[i][j] = Float.POSITIVE_INFINITY;
                    pathLengths[j][i] = Float.POSITIVE_INFINITY;
                    edgeCosts[i][j] = Float.POSITIVE_INFINITY;
                    edgeCosts[j][i] = Float.POSITIVE_INFINITY;
                    shortestPaths[i][j] = new ArrayList<Point>();
                    shortestPaths[j][i] = new ArrayList<Point>();
                }
            }
        }
    }
