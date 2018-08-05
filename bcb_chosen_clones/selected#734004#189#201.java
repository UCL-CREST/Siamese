    private void updateWeightTiming(int trackID, int k, float ts, boolean increase) {
        if (increase) {
            double dt = Math.min(ts - spiketimes[k][0], ts - spiketimes[k][1]);
            double kernel = (Math.exp(-Math.abs(1e-6 * dt / kernelConst))) - kernelOffset;
            w[trackID][k] += timingAlpha * kernel;
            if (w[trackID][k] < -1.0f) w[trackID][k] = -1.0f; else if (w[trackID][k] > 1.0f) w[trackID][k] = 1.0f;
            w[k][trackID] = w[trackID][k];
        } else {
            w[trackID][k] -= reduceW;
            if (w[trackID][k] < -1.0f) w[trackID][k] = -1.0f;
            w[k][trackID] = w[trackID][k];
        }
    }
