    private void zipProblemData(File outputFile, Problem problem) throws PersistenceException, JudgeServerErrorException, ProblemDataErrorException {
        try {
            ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(outputFile));
            try {
                List<Reference> inputFiles = this.referenceDAO.getProblemReferences(problem.getId(), ReferenceType.INPUT);
                List<Reference> outputFiles = this.referenceDAO.getProblemReferences(problem.getId(), ReferenceType.OUTPUT);
                if (inputFiles.size() != outputFiles.size() && inputFiles.size() > 0 && outputFiles.size() > 0) {
                    throw new ProblemDataErrorException("Unequal number of inputs and outputs for problem " + problem.getId());
                }
                for (Reference input : inputFiles) {
                    if (input.getContent() == null) {
                        throw new ProblemDataErrorException("Can not find content for input with reference id " + input.getId());
                    }
                }
                for (Reference output : outputFiles) {
                    if (output.getContent() == null) {
                        throw new ProblemDataErrorException("Can not find content for output with reference id " + output.getId());
                    }
                }
                Reference specialJudge = null;
                if (problem.isChecker()) {
                    List<Reference> specialJudges = this.referenceDAO.getProblemReferences(problem.getId(), ReferenceType.CHECKER_SOURCE);
                    if (specialJudges.size() == 0) {
                        throw new ProblemDataErrorException("Can not find special judge for problem " + problem.getId());
                    }
                    if (specialJudges.size() > 1) {
                        throw new ProblemDataErrorException("Find more than one special judge for problem " + problem.getId());
                    }
                    specialJudge = specialJudges.get(0);
                    String contentType = specialJudge.getContentType();
                    if (contentType == null) {
                        throw new ProblemDataErrorException("Can not find source content type for special judge with reference id " + specialJudge.getId());
                    }
                    byte[] content = specialJudge.getContent();
                    if (content == null) {
                        throw new ProblemDataErrorException("Can not find source content for special judge with reference id " + specialJudge.getId());
                    }
                    if (content.length == 0) {
                        throw new ProblemDataErrorException("Empty source for special judge with reference id " + specialJudge.getId());
                    }
                }
                for (int i = 0; i < inputFiles.size(); i++) {
                    zipOut.putNextEntry(new ZipEntry(String.format("%d.in", i + 1)));
                    CopyUtils.copy(inputFiles.get(i).getContent(), zipOut);
                }
                for (int i = 0; i < outputFiles.size(); i++) {
                    zipOut.putNextEntry(new ZipEntry(String.format("%d.out", i + 1)));
                    CopyUtils.copy(outputFiles.get(i).getContent(), zipOut);
                }
                if (specialJudge != null) {
                    zipOut.putNextEntry(new ZipEntry(String.format("judge.%s", specialJudge.getContentType())));
                    CopyUtils.copy(specialJudge.getContent(), zipOut);
                }
            } finally {
                zipOut.close();
            }
        } catch (IOException e) {
            throw new JudgeServerErrorException("Fail to zip problem data", e);
        }
    }
