    private int findNearest(double target) {
        if (target < this.axisVals.get(0).value || target > this.axisVals.get(this.axisVals.size() - 1).value) {
            return -1;
        }
        int low = 0;
        int high = this.axisVals.size() - 1;
        while (high > low + 1) {
            int mid = (low + high) / 2;
            AxisValue midVal = this.axisVals.get(mid);
            if (midVal.value == target) return midVal.index; else if (midVal.value < target) low = mid; else high = mid;
        }
        AxisValue lowVal = this.axisVals.get(low);
        AxisValue highVal = this.axisVals.get(high);
        return (Math.abs(target - lowVal.value) < Math.abs(target - highVal.value)) ? lowVal.index : highVal.index;
    }
