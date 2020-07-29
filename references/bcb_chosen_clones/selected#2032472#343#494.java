    public ActionForward uploadFile(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse in_response) {
        ActionMessages errors = new ActionMessages();
        ActionMessages messages = new ActionMessages();
        String returnPage = "submitPocketSampleInformationPage";
        UploadForm form = (UploadForm) actForm;
        Integer shippingId = null;
        try {
            eHTPXXLSParser parser = new eHTPXXLSParser();
            String proposalCode;
            String proposalNumber;
            String proposalName;
            String uploadedFileName;
            String realXLSPath;
            if (request != null) {
                proposalCode = (String) request.getSession().getAttribute(Constants.PROPOSAL_CODE);
                proposalNumber = String.valueOf(request.getSession().getAttribute(Constants.PROPOSAL_NUMBER));
                proposalName = proposalCode + proposalNumber.toString();
                uploadedFileName = form.getRequestFile().getFileName();
                String fileName = proposalName + "_" + uploadedFileName;
                realXLSPath = request.getRealPath("\\tmp\\") + "\\" + fileName;
                FormFile f = form.getRequestFile();
                InputStream in = f.getInputStream();
                File outputFile = new File(realXLSPath);
                if (outputFile.exists()) outputFile.delete();
                FileOutputStream out = new FileOutputStream(outputFile);
                while (in.available() != 0) {
                    out.write(in.read());
                    out.flush();
                }
                out.flush();
                out.close();
            } else {
                proposalCode = "ehtpx";
                proposalNumber = "1";
                proposalName = proposalCode + proposalNumber.toString();
                uploadedFileName = "ispyb-template41.xls";
                realXLSPath = "D:\\" + uploadedFileName;
            }
            FileInputStream inFile = new FileInputStream(realXLSPath);
            parser.retrieveShippingId(realXLSPath);
            shippingId = parser.getShippingId();
            String requestShippingId = form.getShippingId();
            if (requestShippingId != null && !requestShippingId.equals("")) {
                shippingId = new Integer(requestShippingId);
            }
            ClientLogger.getInstance().debug("uploadFile for shippingId " + shippingId);
            if (shippingId != null) {
                Log.debug(" ---[uploadFile] Upload for Existing Shipment (DewarTRacking): Deleting Samples from Shipment :");
                double nbSamplesContainers = DBAccess_EJB.DeleteAllSamplesAndContainersForShipping(shippingId);
                if (nbSamplesContainers > 0) parser.getValidationWarnings().add(new XlsUploadException("Shipment contained Samples and/or Containers", "Previous Samples and/or Containers have been deleted and replaced by new ones.")); else parser.getValidationWarnings().add(new XlsUploadException("Shipment contained no Samples and no Containers", "Samples and Containers have been added."));
            }
            Hashtable<String, Hashtable<String, Integer>> listProteinAcronym_SampleName = new Hashtable<String, Hashtable<String, Integer>>();
            ProposalFacadeLocal proposal = ProposalFacadeUtil.getLocalHome().create();
            ProteinFacadeLocal protein = ProteinFacadeUtil.getLocalHome().create();
            CrystalFacadeLocal crystal = CrystalFacadeUtil.getLocalHome().create();
            ProposalLightValue targetProposal = (ProposalLightValue) (((ArrayList) proposal.findByCodeAndNumber(proposalCode, new Integer(proposalNumber))).get(0));
            ArrayList listProteins = (ArrayList) protein.findByProposalId(targetProposal.getProposalId());
            for (int p = 0; p < listProteins.size(); p++) {
                ProteinValue prot = (ProteinValue) listProteins.get(p);
                Hashtable<String, Integer> listSampleName = new Hashtable<String, Integer>();
                CrystalLightValue listCrystals[] = prot.getCrystals();
                for (int c = 0; c < listCrystals.length; c++) {
                    CrystalLightValue _xtal = (CrystalLightValue) listCrystals[c];
                    CrystalValue xtal = crystal.findByPrimaryKey(_xtal.getPrimaryKey());
                    BlsampleLightValue listSamples[] = xtal.getBlsamples();
                    for (int s = 0; s < listSamples.length; s++) {
                        BlsampleLightValue sample = listSamples[s];
                        listSampleName.put(sample.getName(), sample.getBlSampleId());
                    }
                }
                listProteinAcronym_SampleName.put(prot.getAcronym(), listSampleName);
            }
            parser.validate(inFile, listProteinAcronym_SampleName, targetProposal.getProposalId());
            List listErrors = parser.getValidationErrors();
            List listWarnings = parser.getValidationWarnings();
            if (listErrors.size() == 0) {
                parser.open(realXLSPath);
                if (parser.getCrystals().size() == 0) {
                    parser.getValidationErrors().add(new XlsUploadException("No crystals have been found", "Empty shipment"));
                }
            }
            Iterator errIt = listErrors.iterator();
            while (errIt.hasNext()) {
                XlsUploadException xlsEx = (XlsUploadException) errIt.next();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", xlsEx.getMessage() + " ---> " + xlsEx.getSuggestedFix()));
            }
            try {
                saveErrors(request, errors);
            } catch (Exception e) {
            }
            Iterator warnIt = listWarnings.iterator();
            while (warnIt.hasNext()) {
                XlsUploadException xlsEx = (XlsUploadException) warnIt.next();
                messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.free", xlsEx.getMessage() + " ---> " + xlsEx.getSuggestedFix()));
            }
            try {
                saveMessages(request, messages);
            } catch (Exception e) {
            }
            if (listErrors.size() > 0) {
                resetCounts(shippingId);
                return mapping.findForward("submitPocketSampleInformationPage");
            }
            if (listWarnings.size() > 0) returnPage = "submitPocketSampleInformationPage";
            String crystalDetailsXML;
            XtalDetails xtalDetailsWebService = new XtalDetails();
            CrystalDetailsBuilder cDE = new CrystalDetailsBuilder();
            CrystalDetailsElement cd = cDE.createCrystalDetailsElement(proposalName, parser.getCrystals());
            cDE.validateJAXBObject(cd);
            crystalDetailsXML = cDE.marshallJaxBObjToString(cd);
            xtalDetailsWebService.submitCrystalDetails(crystalDetailsXML);
            String diffractionPlan;
            DiffractionPlan diffractionPlanWebService = new DiffractionPlan();
            DiffractionPlanBuilder dPB = new DiffractionPlanBuilder();
            Iterator it = parser.getDiffractionPlans().iterator();
            while (it.hasNext()) {
                DiffractionPlanElement dpe = (DiffractionPlanElement) it.next();
                dpe.setProjectUUID(proposalName);
                diffractionPlan = dPB.marshallJaxBObjToString(dpe);
                diffractionPlanWebService.submitDiffractionPlan(diffractionPlan);
            }
            String crystalShipping;
            Shipping shippingWebService = new Shipping();
            CrystalShippingBuilder cSB = new CrystalShippingBuilder();
            Person person = cSB.createPerson("XLS Upload", null, "ISPyB", null, null, "ISPyB", null, "ispyb@esrf.fr", "0000", "0000", null, null);
            Laboratory laboratory = cSB.createLaboratory("Generic Laboratory", "ISPyB Lab", "Sandwich", "Somewhere", "UK", "ISPyB", "ispyb.esrf.fr", person);
            DeliveryAgent deliveryAgent = parser.getDeliveryAgent();
            CrystalShipping cs = cSB.createCrystalShipping(proposalName, laboratory, deliveryAgent, parser.getDewars());
            String shippingName;
            shippingName = uploadedFileName.substring(0, ((uploadedFileName.toLowerCase().lastIndexOf(".xls")) > 0) ? uploadedFileName.toLowerCase().lastIndexOf(".xls") : 0);
            if (shippingName.equalsIgnoreCase("")) shippingName = uploadedFileName.substring(0, ((uploadedFileName.toLowerCase().lastIndexOf(".xlt")) > 0) ? uploadedFileName.toLowerCase().lastIndexOf(".xlt") : 0);
            cs.setName(shippingName);
            crystalShipping = cSB.marshallJaxBObjToString(cs);
            shippingWebService.submitCrystalShipping(crystalShipping, (ArrayList) parser.getDiffractionPlans(), shippingId);
            ServerLogger.Log4Stat("XLS_UPLOAD", proposalName, uploadedFileName);
        } catch (XlsUploadException e) {
            resetCounts(shippingId);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.getMessage()));
            ClientLogger.getInstance().error(e.toString());
            saveErrors(request, errors);
            return mapping.findForward("error");
        } catch (Exception e) {
            resetCounts(shippingId);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
            ClientLogger.getInstance().error(e.toString());
            e.printStackTrace();
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        setCounts(shippingId);
        return mapping.findForward(returnPage);
    }
