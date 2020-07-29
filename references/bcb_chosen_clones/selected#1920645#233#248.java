        void predict() {
            updateVariances();
            deltaTime = (c.getLastEventTimestamp() - latestTimeStampOfLastStep) * 1.e-6f;
            if (c.getLastEventTimestamp() > latestTimeStampOfLastStep) latestTimeStampOfLastStep = c.getLastEventTimestamp();
            F[0][1] = deltaTime;
            F[2][3] = deltaTime;
            B[0][0] = (float) Math.pow(deltaTime, 2) / 2f;
            B[1][1] = deltaTime;
            xp = multMatrix(F, x);
            Q[0][0] = (float) Math.pow(deltaTime, 4) / 4;
            Q[0][1] = (float) Math.pow(deltaTime, 3) / 2;
            Q[1][0] = Q[0][1];
            Q[1][1] = (float) Math.pow(deltaTime, 2);
            Q = multMatrix(Q, processVariance);
            Pp = addMatrix(multMatrix(multMatrix(F, P), transposeMatrix(F)), Q);
        }
