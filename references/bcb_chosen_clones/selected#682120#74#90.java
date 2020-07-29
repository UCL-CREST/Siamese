    public int findSpawnIndex(double u) {
        if (u == this.knots[this.controlPoints.length + 1]) {
            return this.controlPoints.length;
        }
        int low = this.degree;
        int high = this.controlPoints.length + 1;
        int mid = (low + high) / 2;
        while ((u < this.knots[mid]) || (u >= this.knots[mid + 1])) {
            if (u < this.knots[mid]) {
                high = mid;
            } else {
                low = mid;
            }
            mid = (low + high) / 2;
        }
        return mid;
    }
