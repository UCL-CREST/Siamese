    private void updateWeightAnalog(int trackID, int k, float ts, boolean increase) {
        if (increase) {
            float targetV = neurons[trackID].getV(ts);
            float otherV = neurons[k].getV(ts);
            float targetMean = neurons[trackID].getMeanActivation();
            float otherMean = neurons[k].getMeanActivation();
            float delta1 = otherV * (otherV - otherMean) * targetV;
            float delta2 = targetV * (targetV - targetMean) * otherV;
            float mDelta = sigmoid((delta1 + delta2) / 2.0f);
            w[trackID][k] += alpha * mDelta;
            if (w[trackID][k] > 1.0f) w[trackID][k] = 1.0f;
            w[k][trackID] = w[trackID][k];
        } else {
            w[trackID][k] -= reduceW;
            if (w[trackID][k] < -1.0f) w[trackID][k] = -1.0f;
            w[k][trackID] = w[trackID][k];
        }
    }
