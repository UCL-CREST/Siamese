    private void simulate() throws Exception {
        BufferedWriter out = null;
        out = new BufferedWriter(new FileWriter(outFile));
        out.write("#Thread\tReputation\tAction\n");
        out.flush();
        System.out.println("Simulate...");
        File file = new File(trsDemoSimulationfile);
        ObtainUserReputation obtainUserReputationRequest = new ObtainUserReputation();
        ObtainUserReputationResponse obtainUserReputationResponse;
        RateUser rateUserRequest;
        RateUserResponse rateUserResponse;
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String call = br.readLine();
        while (call != null) {
            rateUserRequest = generateRateUserRequest(call);
            try {
                rateUserResponse = trsPort.rateUser(rateUserRequest);
                System.out.println("----------------R A T I N G-------------------");
                System.out.println("VBE: " + rateUserRequest.getVbeId());
                System.out.println("VO: " + rateUserRequest.getVoId());
                System.out.println("USER: " + rateUserRequest.getUserId());
                System.out.println("SERVICE: " + rateUserRequest.getServiceId());
                System.out.println("ACTION: " + rateUserRequest.getActionId());
                System.out.println("OUTCOME: " + rateUserResponse.isOutcome());
                System.out.println("----------------------------------------------");
                assertEquals("The outcome field of the rateUser should be true: MESSAGE=" + rateUserResponse.getMessage(), true, rateUserResponse.isOutcome());
            } catch (RemoteException e) {
                fail(e.getMessage());
            }
            obtainUserReputationRequest.setIoi(null);
            obtainUserReputationRequest.setServiceId(null);
            obtainUserReputationRequest.setUserId(rateUserRequest.getUserId());
            obtainUserReputationRequest.setVbeId(rateUserRequest.getVbeId());
            obtainUserReputationRequest.setVoId(null);
            try {
                obtainUserReputationResponse = trsPort.obtainUserReputation(obtainUserReputationRequest);
                System.out.println("-----------R E P U T A T I O N----------------");
                System.out.println("VBE: " + obtainUserReputationRequest.getVbeId());
                System.out.println("VO: " + obtainUserReputationRequest.getVoId());
                System.out.println("USER: " + obtainUserReputationRequest.getUserId());
                System.out.println("SERVICE: " + obtainUserReputationRequest.getServiceId());
                System.out.println("IOI: " + obtainUserReputationRequest.getIoi());
                System.out.println("REPUTATION: " + obtainUserReputationResponse.getReputation());
                System.out.println("----------------------------------------------");
                assertEquals("The outcome field of the obtainUserReputation should be true: MESSAGE=" + obtainUserReputationResponse.getMessage(), true, obtainUserReputationResponse.isOutcome());
                assertEquals(0.0, obtainUserReputationResponse.getReputation(), 1.0);
            } catch (RemoteException e) {
                fail(e.getMessage());
            }
            obtainUserReputationRequest.setIoi(null);
            obtainUserReputationRequest.setServiceId(null);
            obtainUserReputationRequest.setUserId(rateUserRequest.getUserId());
            obtainUserReputationRequest.setVbeId(rateUserRequest.getVbeId());
            obtainUserReputationRequest.setVoId(rateUserRequest.getVoId());
            try {
                obtainUserReputationResponse = trsPort.obtainUserReputation(obtainUserReputationRequest);
                System.out.println("-----------R E P U T A T I O N----------------");
                System.out.println("VBE: " + obtainUserReputationRequest.getVbeId());
                System.out.println("VO: " + obtainUserReputationRequest.getVoId());
                System.out.println("USER: " + obtainUserReputationRequest.getUserId());
                System.out.println("SERVICE: " + obtainUserReputationRequest.getServiceId());
                System.out.println("IOI: " + obtainUserReputationRequest.getIoi());
                System.out.println("REPUTATION: " + obtainUserReputationResponse.getReputation());
                System.out.println("----------------------------------------------");
                assertEquals("The outcome field of the obtainUserReputation should be true: MESSAGE=" + obtainUserReputationResponse.getMessage(), true, obtainUserReputationResponse.isOutcome());
                assertEquals(0.0, obtainUserReputationResponse.getReputation(), 1.0);
            } catch (RemoteException e) {
                fail(e.getMessage());
            }
            call = br.readLine();
        }
        fis.close();
        br.close();
        out.flush();
        out.close();
    }
