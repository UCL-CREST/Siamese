    public void updatePheromones(Population pop) {
        synchronized (this) {
            for (int i = 0; i < pheromone.length; i++) {
                for (int j = i; j < pheromone.length; j++) {
                    pheromone[i][j] *= p;
                    pheromone[j][i] = pheromone[i][j];
                }
            }
            for (int k = 0; k < pop.getPopulationSize(); k++) {
                Ant ant = (Ant) pop.get(k);
                for (int i = 0; i < ant.getRepresentationLength() - 1; i++) {
                    pheromone[ant.getGenAt(i) - 1][ant.getGenAt(i + 1) - 1] += q / ant.getValue();
                    pheromone[ant.getGenAt(i + 1) - 1][ant.getGenAt(i) - 1] = pheromone[ant.getGenAt(i) - 1][ant.getGenAt(i + 1) - 1];
                }
            }
        }
    }
