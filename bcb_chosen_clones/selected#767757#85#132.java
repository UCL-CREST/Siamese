    public void restart() {
        theStartApp.log("Dealer.restart()", 3);
        if (theStartApp.dealingGames.size() == 0) return;
        int index = -1;
        int numGames = theStartApp.dealingGames.size();
        int r = (int) (((double) numGames) * Math.random() + 1.0);
        boolean gameDealt = false;
        for (int i = 0; i < numGames; i++) {
            int sum = 0;
            for (int j = 0; j <= i; j++) sum++;
            if ((r <= sum) && (!gameDealt)) {
                String gameName = new String();
                String gameTitle = new String();
                for (int g = 0; g < theStartApp.getGameClasses().size(); g++) {
                    if (((String) theStartApp.dealingGames.get(i)).equals((String) theStartApp.getGameClasses().get(g))) {
                        gameName = (String) theStartApp.dealingGames.get(i);
                        if (theStartApp.noLimit) {
                            gameTitle = "No Limit " + gameName;
                        } else if (theStartApp.betLimit) {
                            gameTitle = "" + theStartApp.maximumBet + " limit " + gameName;
                        } else if (theStartApp.potLimit) {
                            gameTitle = "Pot Limit " + gameName;
                        } else {
                            PokerMoney d = new PokerMoney(2.0f * theStartApp.minimumBet.amount());
                            gameTitle = "" + theStartApp.minimumBet + "/" + d + " " + gameName;
                        }
                        theStartApp.broadcastMessage("NEW GAME  &" + gameTitle);
                        try {
                            Class game_class = Class.forName("net.sourceforge.pokerapp.games." + theStartApp.getGameClasses().get(g));
                            Class arg_types[] = { Class.forName(theStartApp.getClass().getName()) };
                            Constructor construct = game_class.getConstructor(arg_types);
                            Object argList[] = { theStartApp };
                            theStartApp.setGame((PokerGame) construct.newInstance(argList));
                            gameDealt = true;
                        } catch (Exception x) {
                            theStartApp.log("Error : Dealer tried to deal a game of " + gameTitle + ", but caught an exception.");
                            theStartApp.logStackTrace(x);
                        }
                        if (theStartApp.getGame() != null) {
                            if (theStartApp.getGame().getGameError()) {
                                theStartApp.nullifyGame();
                            }
                        }
                    }
                }
            }
        }
    }
