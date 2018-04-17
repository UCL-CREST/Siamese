    private void generateGames(int multiRounds) {
        StdRegularSeasonRules rules = new StdRegularSeasonRules();
        int teamCount = teams.length;
        boolean zusatzTeam = false;
        if (teams.length % 2 != 0) {
            teamCount++;
            zusatzTeam = true;
        }
        this.gamesPerRound = teamCount / 2;
        int scheduleRound = 0;
        int[][] table = new int[teamCount][teamCount];
        for (int i = 0; i < teamCount; i++) {
            for (int j = 0; j < teamCount; j++) {
                if (i == j) {
                    table[i][j] = -1;
                } else {
                    if (j == teamCount - 1) {
                        table[i][j] = (j + 2 * i);
                        while (table[i][j] >= teamCount) {
                            table[i][j] -= (teamCount - 1);
                        }
                    } else {
                        if (j > i) {
                            table[i][j] = i + j;
                            while (table[i][j] >= teamCount) {
                                table[i][j] -= (teamCount - 1);
                            }
                        } else {
                            table[i][j] = table[j][i] + teamCount - 1;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < teamCount; i++) {
            for (int j = i; j < teamCount; j++) {
                scheduleRound = table[i][j];
                if (!(scheduleRound % 2 == 0)) {
                    table[i][j] = table[j][i];
                    table[j][i] = scheduleRound;
                }
            }
        }
        int rounds = (teamCount * 2 - 2) * multiRounds;
        if (zusatzTeam) {
            gamesPerRound--;
        }
        schedule = new Match[rounds][gamesPerRound];
        int games = 0;
        for (int round = 0; round < teamCount * 2 - 1; round++) {
            for (int i = 0; i < teamCount; i++) {
                if (!zusatzTeam || i != teamCount - 1) {
                    for (int j = 0; j < teamCount; j++) {
                        if (!zusatzTeam || j != teamCount - 1) {
                            scheduleRound = table[i][j];
                            if (scheduleRound != -1 && round == scheduleRound) {
                                for (int mr = 0; mr < multiRounds; mr++) {
                                    schedule[scheduleRound - 1 + (rounds / multiRounds) * mr][games] = this.createMatch(this, teams[i], teams[j], rules);
                                }
                                games++;
                                if (games == gamesPerRound) {
                                    games = 0;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
