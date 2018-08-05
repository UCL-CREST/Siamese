    public SWORDEntry ingestDepost(final DepositCollection pDeposit, final ServiceDocument pServiceDocument) throws SWORDException {
        try {
            ZipFileAccess tZipFile = new ZipFileAccess(super.getTempDir());
            LOG.debug("copying file");
            String tZipTempFileName = super.getTempDir() + "uploaded-file.tmp";
            IOUtils.copy(pDeposit.getFile(), new FileOutputStream(tZipTempFileName));
            Datastream tDatastream = new LocalDatastream(super.getGenericFileName(pDeposit), this.getContentType(), tZipTempFileName);
            _datastreamList.add(tDatastream);
            _datastreamList.addAll(tZipFile.getFiles(tZipTempFileName));
            int i = 0;
            boolean found = false;
            for (i = 0; i < _datastreamList.size(); i++) {
                if (_datastreamList.get(i).getId().equalsIgnoreCase("mets")) {
                    found = true;
                    break;
                }
            }
            if (found) {
                SAXBuilder tBuilder = new SAXBuilder();
                _mets = new METSObject(tBuilder.build(((LocalDatastream) _datastreamList.get(i)).getPath()));
                LocalDatastream tLocalMETSDS = (LocalDatastream) _datastreamList.remove(i);
                new File(tLocalMETSDS.getPath()).delete();
                _datastreamList.add(_mets.getMETSDs());
                _datastreamList.addAll(_mets.getMetadataDatastreams());
            } else {
                throw new SWORDException("Couldn't find a METS document in the zip file, ensure it is named mets.xml or METS.xml");
            }
            SWORDEntry tEntry = super.ingestDepost(pDeposit, pServiceDocument);
            tZipFile.removeLocalFiles();
            return tEntry;
        } catch (IOException tIOExcpt) {
            String tMessage = "Couldn't retrieve METS from deposit: " + tIOExcpt.toString();
            LOG.error(tMessage);
            tIOExcpt.printStackTrace();
            throw new SWORDException(tMessage, tIOExcpt);
        } catch (JDOMException tJDOMExcpt) {
            String tMessage = "Couldn't build METS from deposit: " + tJDOMExcpt.toString();
            LOG.error(tMessage);
            tJDOMExcpt.printStackTrace();
            throw new SWORDException(tMessage, tJDOMExcpt);
        }
    }
