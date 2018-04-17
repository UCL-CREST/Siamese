    public SOCTradeOffer makeOffer(SOCPossiblePiece targetPiece) {
        D.ebugPrintln("***** MAKE OFFER *****");
        if (targetPiece == null) {
            return null;
        }
        SOCTradeOffer offer = null;
        SOCResourceSet targetResources = null;
        switch(targetPiece.getType()) {
            case SOCPossiblePiece.CARD:
                targetResources = SOCGame.CARD_SET;
                break;
            case SOCPossiblePiece.ROAD:
                targetResources = SOCGame.ROAD_SET;
                break;
            case SOCPossiblePiece.SETTLEMENT:
                targetResources = SOCGame.SETTLEMENT_SET;
                break;
            case SOCPossiblePiece.CITY:
                targetResources = SOCGame.CITY_SET;
                break;
        }
        SOCResourceSet ourResources = ourPlayerData.getResources();
        D.ebugPrintln("*** targetResources = " + targetResources);
        D.ebugPrintln("*** ourResources = " + ourResources);
        if (ourResources.contains(targetResources)) {
            return offer;
        }
        if (ourResources.getAmount(SOCResourceConstants.UNKNOWN) > 0) {
            D.ebugPrintln("AGG WE HAVE UNKNOWN RESOURCES !!!! %%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            return offer;
        }
        SOCTradeOffer batna = getOfferToBank(targetResources);
        D.ebugPrintln("*** BATNA = " + batna);
        SOCBuildingSpeedEstimate estimate = new SOCBuildingSpeedEstimate(ourPlayerData.getNumbers());
        SOCResourceSet giveResourceSet = new SOCResourceSet();
        SOCResourceSet getResourceSet = new SOCResourceSet();
        int batnaBuildingTime = getETAToTargetResources(ourPlayerData, targetResources, giveResourceSet, getResourceSet, estimate);
        D.ebugPrintln("*** batnaBuildingTime = " + batnaBuildingTime);
        if (batna != null) {
            batnaBuildingTime = getETAToTargetResources(ourPlayerData, targetResources, batna.getGiveSet(), batna.getGetSet(), estimate);
        }
        D.ebugPrintln("*** batnaBuildingTime = " + batnaBuildingTime);
        int[] rollsPerResource = estimate.getRollsPerResource();
        int[] neededRsrc = new int[5];
        int[] notNeededRsrc = new int[5];
        int neededRsrcCount = 0;
        int notNeededRsrcCount = 0;
        for (int rsrcType = SOCResourceConstants.CLAY; rsrcType <= SOCResourceConstants.WOOD; rsrcType++) {
            if (targetResources.getAmount(rsrcType) > 0) {
                neededRsrc[neededRsrcCount] = rsrcType;
                neededRsrcCount++;
            } else {
                notNeededRsrc[notNeededRsrcCount] = rsrcType;
                notNeededRsrcCount++;
            }
        }
        for (int j = neededRsrcCount - 1; j >= 0; j--) {
            for (int i = 0; i < j; i++) {
                if (rollsPerResource[neededRsrc[i]] > rollsPerResource[neededRsrc[i + 1]]) {
                    int tmp = neededRsrc[i];
                    neededRsrc[i] = neededRsrc[i + 1];
                    neededRsrc[i + 1] = tmp;
                }
            }
        }
        if (D.ebugOn) {
            for (int i = 0; i < neededRsrcCount; i++) {
                D.ebugPrintln("NEEDED RSRC: " + neededRsrc[i] + " : " + rollsPerResource[neededRsrc[i]]);
            }
        }
        for (int j = notNeededRsrcCount - 1; j >= 0; j--) {
            for (int i = 0; i < j; i++) {
                if (rollsPerResource[notNeededRsrc[i]] > rollsPerResource[notNeededRsrc[i + 1]]) {
                    int tmp = notNeededRsrc[i];
                    notNeededRsrc[i] = notNeededRsrc[i + 1];
                    notNeededRsrc[i + 1] = tmp;
                }
            }
        }
        if (D.ebugOn) {
            for (int i = 0; i < notNeededRsrcCount; i++) {
                D.ebugPrintln("NOT-NEEDED RSRC: " + notNeededRsrc[i] + " : " + rollsPerResource[notNeededRsrc[i]]);
            }
        }
        boolean[] someoneIsSellingResource = new boolean[SOCResourceConstants.MAXPLUSONE];
        for (int rsrcType = SOCResourceConstants.CLAY; rsrcType <= SOCResourceConstants.WOOD; rsrcType++) {
            someoneIsSellingResource[rsrcType] = false;
            for (int pn = 0; pn < SOCGame.MAXPLAYERS; pn++) {
                if ((pn != ourPlayerData.getPlayerNumber()) && (isSellingResource[pn][rsrcType])) {
                    someoneIsSellingResource[rsrcType] = true;
                    D.ebugPrintln("*** player " + pn + " is selling " + rsrcType);
                    break;
                }
            }
        }
        int getRsrcIdx = neededRsrcCount - 1;
        while ((getRsrcIdx >= 0) && ((ourResources.getAmount(neededRsrc[getRsrcIdx]) >= targetResources.getAmount(neededRsrc[getRsrcIdx])) || (!someoneIsSellingResource[neededRsrc[getRsrcIdx]]))) {
            getRsrcIdx--;
        }
        if (getRsrcIdx >= 0) {
            D.ebugPrintln("*** getRsrc = " + neededRsrc[getRsrcIdx]);
            getResourceSet.add(1, neededRsrc[getRsrcIdx]);
            D.ebugPrintln("*** offer should be null : offer = " + offer);
            int giveRsrcIdx = 0;
            while ((giveRsrcIdx < notNeededRsrcCount) && (offer == null)) {
                D.ebugPrintln("*** ourResources.getAmount(" + notNeededRsrc[giveRsrcIdx] + ") = " + ourResources.getAmount(notNeededRsrc[giveRsrcIdx]));
                if (ourResources.getAmount(notNeededRsrc[giveRsrcIdx]) > 0) {
                    giveResourceSet.clear();
                    giveResourceSet.add(1, notNeededRsrc[giveRsrcIdx]);
                    offer = makeOfferAux(giveResourceSet, getResourceSet, neededRsrc[getRsrcIdx]);
                    D.ebugPrintln("*** offer = " + offer);
                    int offerBuildingTime = getETAToTargetResources(ourPlayerData, targetResources, giveResourceSet, getResourceSet, estimate);
                    D.ebugPrintln("*** offerBuildingTime = " + offerBuildingTime);
                }
                giveRsrcIdx++;
            }
            D.ebugPrintln("*** ourResources = " + ourResources);
            if (offer == null) {
                int giveRsrcIdx1 = 0;
                while ((giveRsrcIdx1 < neededRsrcCount) && (offer == null)) {
                    D.ebugPrintln("*** ourResources.getAmount(" + neededRsrc[giveRsrcIdx1] + ") = " + ourResources.getAmount(neededRsrc[giveRsrcIdx1]));
                    D.ebugPrintln("*** targetResources.getAmount(" + neededRsrc[giveRsrcIdx1] + ") = " + targetResources.getAmount(neededRsrc[giveRsrcIdx1]));
                    if ((ourResources.getAmount(neededRsrc[giveRsrcIdx1]) > targetResources.getAmount(neededRsrc[giveRsrcIdx1])) && (neededRsrc[giveRsrcIdx1] != neededRsrc[getRsrcIdx])) {
                        giveResourceSet.clear();
                        giveResourceSet.add(1, neededRsrc[giveRsrcIdx1]);
                        int offerBuildingTime = getETAToTargetResources(ourPlayerData, targetResources, giveResourceSet, getResourceSet, estimate);
                        if ((offerBuildingTime < batnaBuildingTime) || ((batna != null) && (offerBuildingTime == batnaBuildingTime) && (giveResourceSet.getTotal() < batna.getGiveSet().getTotal()))) {
                            offer = makeOfferAux(giveResourceSet, getResourceSet, neededRsrc[getRsrcIdx]);
                            D.ebugPrintln("*** offer = " + offer);
                            D.ebugPrintln("*** offerBuildingTime = " + offerBuildingTime);
                        }
                    }
                    giveRsrcIdx1++;
                }
            }
            D.ebugPrintln("*** ourResources = " + ourResources);
            SOCResourceSet leftovers = ourResources.copy();
            leftovers.subtract(targetResources);
            D.ebugPrintln("*** leftovers = " + leftovers);
            if (offer == null) {
                int giveRsrcIdx1 = 0;
                int giveRsrcIdx2 = 0;
                while ((giveRsrcIdx1 < notNeededRsrcCount) && (offer == null)) {
                    if (ourResources.getAmount(notNeededRsrc[giveRsrcIdx1]) > 0) {
                        while ((giveRsrcIdx2 < notNeededRsrcCount) && (offer == null)) {
                            giveResourceSet.clear();
                            giveResourceSet.add(1, notNeededRsrc[giveRsrcIdx1]);
                            giveResourceSet.add(1, notNeededRsrc[giveRsrcIdx2]);
                            if (ourResources.contains(giveResourceSet)) {
                                int offerBuildingTime = getETAToTargetResources(ourPlayerData, targetResources, giveResourceSet, getResourceSet, estimate);
                                if ((offerBuildingTime < batnaBuildingTime) || ((batna != null) && (offerBuildingTime == batnaBuildingTime) && (giveResourceSet.getTotal() < batna.getGiveSet().getTotal()))) {
                                    offer = makeOfferAux(giveResourceSet, getResourceSet, neededRsrc[getRsrcIdx]);
                                    D.ebugPrintln("*** offer = " + offer);
                                    D.ebugPrintln("*** offerBuildingTime = " + offerBuildingTime);
                                }
                            }
                            giveRsrcIdx2++;
                        }
                        giveRsrcIdx2 = 0;
                        while ((giveRsrcIdx2 < neededRsrcCount) && (offer == null)) {
                            if (neededRsrc[giveRsrcIdx2] != neededRsrc[getRsrcIdx]) {
                                giveResourceSet.clear();
                                giveResourceSet.add(1, notNeededRsrc[giveRsrcIdx1]);
                                giveResourceSet.add(1, neededRsrc[giveRsrcIdx2]);
                                if (leftovers.contains(giveResourceSet)) {
                                    int offerBuildingTime = getETAToTargetResources(ourPlayerData, targetResources, giveResourceSet, getResourceSet, estimate);
                                    if ((offerBuildingTime < batnaBuildingTime) || ((batna != null) && (offerBuildingTime == batnaBuildingTime) && (giveResourceSet.getTotal() < batna.getGiveSet().getTotal()))) {
                                        offer = makeOfferAux(giveResourceSet, getResourceSet, neededRsrc[getRsrcIdx]);
                                        D.ebugPrintln("*** offer = " + offer);
                                        D.ebugPrintln("*** offerBuildingTime = " + offerBuildingTime);
                                    }
                                }
                            }
                            giveRsrcIdx2++;
                        }
                    }
                    giveRsrcIdx1++;
                }
                giveRsrcIdx1 = 0;
                giveRsrcIdx2 = 0;
                while ((giveRsrcIdx1 < neededRsrcCount) && (offer == null)) {
                    if ((leftovers.getAmount(neededRsrc[giveRsrcIdx1]) > 0) && (neededRsrc[giveRsrcIdx1] != neededRsrc[getRsrcIdx])) {
                        while ((giveRsrcIdx2 < notNeededRsrcCount) && (offer == null)) {
                            giveResourceSet.clear();
                            giveResourceSet.add(1, neededRsrc[giveRsrcIdx1]);
                            giveResourceSet.add(1, notNeededRsrc[giveRsrcIdx2]);
                            if (leftovers.contains(giveResourceSet)) {
                                int offerBuildingTime = getETAToTargetResources(ourPlayerData, targetResources, giveResourceSet, getResourceSet, estimate);
                                if ((offerBuildingTime < batnaBuildingTime) || ((batna != null) && (offerBuildingTime == batnaBuildingTime) && (giveResourceSet.getTotal() < batna.getGiveSet().getTotal()))) {
                                    offer = makeOfferAux(giveResourceSet, getResourceSet, neededRsrc[getRsrcIdx]);
                                    D.ebugPrintln("*** offer = " + offer);
                                    D.ebugPrintln("*** offerBuildingTime = " + offerBuildingTime);
                                }
                            }
                            giveRsrcIdx2++;
                        }
                        giveRsrcIdx2 = 0;
                        while ((giveRsrcIdx2 < neededRsrcCount) && (offer == null)) {
                            if (neededRsrc[giveRsrcIdx2] != neededRsrc[getRsrcIdx]) {
                                giveResourceSet.clear();
                                giveResourceSet.add(1, neededRsrc[giveRsrcIdx1]);
                                giveResourceSet.add(1, neededRsrc[giveRsrcIdx2]);
                                if (leftovers.contains(giveResourceSet)) {
                                    int offerBuildingTime = getETAToTargetResources(ourPlayerData, targetResources, giveResourceSet, getResourceSet, estimate);
                                    if ((offerBuildingTime < batnaBuildingTime) || ((batna != null) && (offerBuildingTime == batnaBuildingTime) && (giveResourceSet.getTotal() < batna.getGiveSet().getTotal()))) {
                                        offer = makeOfferAux(giveResourceSet, getResourceSet, neededRsrc[getRsrcIdx]);
                                        D.ebugPrintln("*** offer = " + offer);
                                        D.ebugPrintln("*** offerBuildingTime = " + offerBuildingTime);
                                    }
                                }
                            }
                            giveRsrcIdx2++;
                        }
                    }
                    giveRsrcIdx1++;
                }
            }
        }
        if (offer == null) {
            SOCResourceSet leftovers = ourResources.copy();
            leftovers.subtract(targetResources);
            D.ebugPrintln("*** leftovers = " + leftovers);
            int getRsrcIdx2 = notNeededRsrcCount - 1;
            while ((getRsrcIdx2 >= 0) && (!someoneIsSellingResource[neededRsrc[getRsrcIdx2]])) {
                getRsrcIdx2--;
            }
            while ((getRsrcIdx2 >= 0) && (offer == null)) {
                getResourceSet.clear();
                getResourceSet.add(1, notNeededRsrc[getRsrcIdx2]);
                leftovers.add(1, notNeededRsrc[getRsrcIdx2]);
                if (offer == null) {
                    int giveRsrcIdx1 = 0;
                    while ((giveRsrcIdx1 < notNeededRsrcCount) && (offer == null)) {
                        if ((leftovers.getAmount(notNeededRsrc[giveRsrcIdx1]) > 0) && (notNeededRsrc[giveRsrcIdx1] != notNeededRsrc[getRsrcIdx2])) {
                            leftovers.subtract(1, notNeededRsrc[giveRsrcIdx1]);
                            if (getOfferToBank(targetResources, leftovers) != null) {
                                giveResourceSet.clear();
                                giveResourceSet.add(1, notNeededRsrc[giveRsrcIdx1]);
                                int offerBuildingTime = getETAToTargetResources(ourPlayerData, targetResources, giveResourceSet, getResourceSet, estimate);
                                if (offerBuildingTime < batnaBuildingTime) {
                                    offer = makeOfferAux(giveResourceSet, getResourceSet, notNeededRsrc[getRsrcIdx2]);
                                    D.ebugPrintln("*** offer = " + offer);
                                    D.ebugPrintln("*** offerBuildingTime = " + offerBuildingTime);
                                }
                            }
                            leftovers.add(1, notNeededRsrc[giveRsrcIdx1]);
                        }
                        giveRsrcIdx1++;
                    }
                }
                if (offer == null) {
                    int giveRsrcIdx1 = 0;
                    while ((giveRsrcIdx1 < neededRsrcCount) && (offer == null)) {
                        if (leftovers.getAmount(neededRsrc[giveRsrcIdx1]) > 0) {
                            leftovers.subtract(1, neededRsrc[giveRsrcIdx1]);
                            if (getOfferToBank(targetResources, leftovers) != null) {
                                giveResourceSet.clear();
                                giveResourceSet.add(1, neededRsrc[giveRsrcIdx1]);
                                int offerBuildingTime = getETAToTargetResources(ourPlayerData, targetResources, giveResourceSet, getResourceSet, estimate);
                                if (offerBuildingTime < batnaBuildingTime) {
                                    offer = makeOfferAux(giveResourceSet, getResourceSet, notNeededRsrc[getRsrcIdx2]);
                                    D.ebugPrintln("*** offer = " + offer);
                                    D.ebugPrintln("*** offerBuildingTime = " + offerBuildingTime);
                                }
                            }
                            leftovers.add(1, neededRsrc[giveRsrcIdx1]);
                        }
                        giveRsrcIdx1++;
                    }
                }
                leftovers.subtract(1, notNeededRsrc[getRsrcIdx2]);
                getRsrcIdx2--;
            }
        }
        return offer;
    }
