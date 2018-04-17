    private GraphEdge searchInChain(List chain, Point p) {
        int r = 0;
        int l = chain.size();
        int py = p.getY();
        while (true) {
            int mid = (r + l) / 2;
            GraphEdge e = (GraphEdge) chain.get(mid);
            int ey1 = e.source.getY();
            int ey2 = e.target.getY();
            if (py >= ey1 && py <= ey2) return e; else if (py < ey1) r = mid; else l = mid;
        }
    }
