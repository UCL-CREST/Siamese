    private void anneal(final float maxGamma, final float gammaAccel, final float objectiveTolerance, final float objectiveAccel, final float scoreTolerance, final float paramTolerance, final float distanceLimit, final float randomLimit, final long randomSeed, final BufferedDocuments<Phrase> references, final int n, final int maxNbest, File stateFile, boolean keepState) {
        float gamma = 0;
        boolean annealObjective = true;
        double[] convergedScores = new double[n];
        double[] totalLogScores = new double[n];
        boolean[] isConverged = new boolean[n];
        GradientPoint[] initPoints = new GradientPoint[n];
        GradientPoint[] prevInitPoints = new GradientPoint[n];
        GradientPoint[] bestInitPoints = new GradientPoint[n];
        GradientPoint[] prevMinPoints = new GradientPoint[n];
        Random rand = new Random(randomSeed);
        Time time = new Time();
        if (stateFile != null && stateFile.length() > 0) {
            time.reset();
            try {
                ObjectInputStream stream = new ObjectInputStream(new FileInputStream(stateFile));
                gamma = stream.readFloat();
                annealObjective = stream.readBoolean();
                convergedScores = (double[]) stream.readObject();
                totalLogScores = (double[]) stream.readObject();
                isConverged = (boolean[]) stream.readObject();
                initPoints = (GradientPoint[]) stream.readObject();
                prevInitPoints = (GradientPoint[]) stream.readObject();
                bestInitPoints = (GradientPoint[]) stream.readObject();
                prevMinPoints = (GradientPoint[]) stream.readObject();
                rand = (Random) stream.readObject();
                int size = stream.readInt();
                for (int id = 0; id < size; id++) {
                    Feature feature = FEATURES.getRaw(CONFIG, stream.readUTF(), 0f);
                    if (feature.getId() != id) throw new Exception("Features have changed");
                }
                evaluation.read(stream);
                stream.close();
                output.println("# Resuming from previous optimization state (" + time + ")");
                output.println();
            } catch (Exception e) {
                e.printStackTrace();
                Log.getInstance().severe("Failed loading optimization state (" + stateFile + "): " + e.getMessage());
            }
        } else {
            final int evaluations = ProjectedEvaluation.CFG_OPT_HISTORY_SIZE.getValue();
            final GradientPoint[] randPoints = new GradientPoint[n * evaluations];
            for (int i = 0; i < n; i++) {
                evaluation.setParallelId(i);
                for (int j = 0; j < evaluations; j++) {
                    if (i != 0) randPoints[i * n + j] = getRandomPoint(rand, randPoints[0], distanceLimit, null);
                    evaluate(references, i + ":" + j);
                    if (i == 0) {
                        randPoints[0] = new GradientPoint(evaluation, null);
                        gamma = LogFeatureModel.FEAT_MODEL_GAMMA.getValue();
                        break;
                    }
                }
            }
            for (int i = 0; i < randPoints.length; i++) if (randPoints[i] != null) randPoints[i] = new GradientPoint(evaluation, randPoints[i], output);
            for (int i = 0; i < n; i++) {
                prevInitPoints[i] = null;
                initPoints[i] = randPoints[i * n];
                if (i != 0) for (int j = 1; j < evaluations; j++) if (randPoints[i * n + j].getScore() < initPoints[i].getScore()) initPoints[i] = randPoints[i * n + j];
                bestInitPoints[i] = initPoints[i];
                convergedScores[i] = Float.MAX_VALUE;
            }
        }
        for (int searchCount = 1; ; searchCount++) {
            boolean isFinished = true;
            for (int i = 0; i < n; i++) isFinished = isFinished && isConverged[i];
            if (isFinished) {
                output.println("*** N-best list converged. Modifying annealing schedule. ***");
                output.println();
                if (annealObjective) {
                    boolean objectiveConverged = true;
                    for (int i = 0; objectiveConverged && i < n; i++) objectiveConverged = isConverged(bestInitPoints[i].getScore(), convergedScores[i], objectiveTolerance, SCORE_EPSILON);
                    annealObjective = false;
                    for (Metric<ProjectedSentenceEvaluation> metric : AbstractEvaluation.CFG_EVAL_METRICS.getValue()) if (metric.doAnnealing()) {
                        float weight = metric.getWeight();
                        if (weight != 0) if (objectiveConverged) metric.setWeight(0); else {
                            annealObjective = true;
                            metric.setWeight(weight / objectiveAccel);
                        }
                    }
                }
                if (!annealObjective) {
                    if (Math.abs(gamma) >= maxGamma) {
                        GradientPoint bestPoint = bestInitPoints[0];
                        for (int i = 1; i < n; i++) if (bestInitPoints[i].getScore() < bestPoint.getScore()) bestPoint = bestInitPoints[i];
                        output.format("Best Score: %+.7g%n", bestPoint.getScore());
                        output.println();
                        bestPoint = new GradientPoint(evaluation, bestPoint, output);
                        break;
                    }
                    gamma *= gammaAccel;
                    if (Math.abs(gamma) + GAMMA_EPSILON >= maxGamma) gamma = gamma >= 0 ? maxGamma : -maxGamma;
                }
                for (int i = 0; i < n; i++) {
                    convergedScores[i] = bestInitPoints[i].getScore();
                    initPoints[i] = new GradientPoint(evaluation, bestInitPoints[i], gamma, output);
                    bestInitPoints[i] = initPoints[i];
                    prevInitPoints[i] = null;
                    prevMinPoints[i] = null;
                    isConverged[i] = false;
                }
                searchCount = 0;
            }
            for (int i = 0; i < n; i++) {
                if (isConverged[i]) continue;
                if (n > 1) output.println("Minimizing point " + i);
                Gradient gradient = initPoints[i].getGradient();
                for (int id = 0; id < FEATURES.size(); id++) output.format("GRAD %-65s %-+13.7g%n", FEATURES.getName(id), gradient.get(id));
                output.println();
                time.reset();
                GradientPoint minPoint = minimize(initPoints[i], prevInitPoints[i], bestInitPoints[i], scoreTolerance, paramTolerance, distanceLimit, randomLimit, rand);
                final float[] weights = minPoint.getWeights();
                for (int j = 0; j < weights.length; j++) output.format("PARM %-65s %-+13.7g%n", FEATURES.getName(j), weights[j]);
                output.println();
                output.format("Minimum Score: %+.7g (average distance of %.2f)%n", minPoint.getScore(), minPoint.getAverageDistance());
                output.println();
                output.println("# Minimized gradient (" + time + ")");
                output.println();
                output.flush();
                isConverged[i] = weights == initPoints[i].getWeights();
                prevInitPoints[i] = initPoints[i];
                prevMinPoints[i] = minPoint;
                initPoints[i] = minPoint;
            }
            for (int i = 0; i < n; i++) {
                if (isConverged[i]) continue;
                isConverged[i] = isConvergedScore("minimum", prevMinPoints[i], prevInitPoints[i], scoreTolerance) && isConvergedWeights(prevMinPoints[i], prevInitPoints[i], paramTolerance);
                prevMinPoints[i].setWeightsAndRescore(evaluation);
                evaluation.setParallelId(i);
                evaluate(references, Integer.toString(i));
            }
            Set<Point> prunePoints = new HashSet<Point>();
            prunePoints.addAll(Arrays.asList(bestInitPoints));
            prunePoints.addAll(Arrays.asList(prevInitPoints));
            prunePoints.addAll(Arrays.asList(initPoints));
            evaluation.prune(prunePoints, maxNbest, output);
            for (int i = 0; i < n; i++) {
                final boolean bestIsPrev = bestInitPoints[i] == prevInitPoints[i];
                final boolean bestIsInit = bestInitPoints[i] == initPoints[i];
                bestInitPoints[i] = new GradientPoint(evaluation, bestInitPoints[i], bestIsInit ? output : null);
                if (bestIsPrev) prevInitPoints[i] = bestInitPoints[i];
                if (bestIsInit) initPoints[i] = bestInitPoints[i];
                if (!bestIsPrev && prevInitPoints[i] != null) {
                    prevInitPoints[i] = new GradientPoint(evaluation, prevInitPoints[i], null);
                    if (prevInitPoints[i].getScore() <= bestInitPoints[i].getScore()) bestInitPoints[i] = prevInitPoints[i];
                }
                if (!bestIsInit) {
                    initPoints[i] = new GradientPoint(evaluation, initPoints[i], output);
                    if (initPoints[i].getScore() <= bestInitPoints[i].getScore()) bestInitPoints[i] = initPoints[i];
                }
            }
            for (int i = 0; i < n; i++) if (isConverged[i]) if (prevMinPoints[i] == null) {
                output.println("# Convergence failed: no previous minimum is defined");
                output.println();
                isConverged[i] = false;
            } else {
                isConverged[i] = isConvergedScore("best known", bestInitPoints[i], initPoints[i], scoreTolerance) && isConvergedScore("previous minimum", prevMinPoints[i], initPoints[i], scoreTolerance);
            }
            if (stateFile != null) {
                time.reset();
                try {
                    File dir = stateFile.getCanonicalFile().getParentFile();
                    File temp = File.createTempFile("cunei-opt-", ".tmp", dir);
                    ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(temp));
                    stream.writeFloat(gamma);
                    stream.writeBoolean(annealObjective);
                    stream.writeObject(convergedScores);
                    stream.writeObject(totalLogScores);
                    stream.writeObject(isConverged);
                    stream.writeObject(initPoints);
                    stream.writeObject(prevInitPoints);
                    stream.writeObject(bestInitPoints);
                    stream.writeObject(prevMinPoints);
                    stream.writeObject(rand);
                    stream.writeInt(FEATURES.size());
                    for (int id = 0; id < FEATURES.size(); id++) stream.writeUTF(FEATURES.getName(id));
                    evaluation.write(stream);
                    stream.close();
                    if (!temp.renameTo(stateFile)) {
                        FileChannel in = null;
                        FileChannel out = null;
                        try {
                            in = new FileInputStream(temp).getChannel();
                            out = new FileOutputStream(stateFile).getChannel();
                            in.transferTo(0, in.size(), out);
                            temp.delete();
                        } finally {
                            if (in != null) in.close();
                            if (out != null) out.close();
                        }
                    }
                    output.println("# Saved optimization state (" + time + ")");
                    output.println();
                } catch (IOException e) {
                    Log.getInstance().severe("Failed writing optimization state: " + e.getMessage());
                }
            }
        }
        if (stateFile != null && !keepState) stateFile.delete();
    }
