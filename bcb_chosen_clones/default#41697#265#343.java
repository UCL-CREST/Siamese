    private static void generateTinyOSTarget() throws IOException {
        String s = null;
        for (NodeInstance node : Spidey.nodesData.values()) {
            BufferedReader reader = new BufferedReader(new FileReader(SPIDEY_REP_TEMPLATE_TINYOS));
            new File(APP_WORKING_DIR + node.getId() + "/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(APP_WORKING_DIR + node.getId() + "/Spidey_" + node.getId() + ".nc"));
            while ((s = reader.readLine()) != null) {
                s = s.concat("\n");
                if (s.indexOf(COMPILER_TAG) != -1) {
                    writer.write(generateCompilerTag());
                } else if (s.indexOf(USER_INCLUDE) != -1) {
                    writer.write(Spidey.include);
                } else if (s.indexOf(SPIDEY_COMPONENT_NAME) != -1) {
                    writer.write("module Spidey_" + node.getId() + " {");
                } else if (s.indexOf(SPIDEY_USES) != -1) {
                    writer.write(Spidey.nesCuses);
                } else if (s.indexOf(USE_COST_TAG) != -1) {
                    writer.write(s.replaceAll(USE_COST_TAG + "\n", node.getUseCost()) + ";\n");
                } else if (s.indexOf(SPIDEY_REP_SIZE) != -1) {
                    writer.write("#define SIZE " + node.getBindings().size() + "\n");
                } else if (s.indexOf(SPIDEY_NODE_ATTRIBUTES) != -1) {
                    int index = 0;
                    for (NodeAttributeBinding binding : node.getBindings()) {
                        writer.write("    if (index_a == " + index + ") {\n      ret.attribute_id = " + lookUpAttribute(binding.getNodeAttribute().getName()).getCdef() + ";\n      ret.attribute_value = " + lookUpValue(binding.getValue()).getCdef() + ";\n    }\n");
                        index++;
                    }
                } else if (s.indexOf(SPIDEY_NGH_INSTANCES) != -1) {
                    for (NghInstance nghInstance : Spidey.nghInstances.values()) {
                        writer.write("    if (neighborhood_id == " + nghInstance.getSpideyId() + ") {\n\n");
                        int actualPredicates = 0;
                        for (NghPredicate nghPredicate : nghInstance.getTemplate().getPredicates()) {
                            String valueCdef = null;
                            if (nghPredicate.isParameter()) {
                                valueCdef = lookUpValue(nghInstance.getActualValue(nghPredicate.getValue())).getCdef();
                            } else {
                                valueCdef = lookUpValue(nghPredicate.getValue()).getCdef();
                            }
                            writer.write("      ln_predicate.attribute_id = " + lookUpAttribute(nghPredicate.getAttributeName()).getCdef() + ";\n      ln_predicate.op_code = " + nghPredicate.getOp() + ";\n      ln_predicate.attribute_value = " + valueCdef + ";\n      ngh.predicates[" + actualPredicates + "] = ln_predicate;\n\n");
                            actualPredicates++;
                        }
                        for (int j = actualPredicates; j < NghTemplate.maxPredicates; j++) {
                            writer.write("      ngh.predicates[" + j + "] = null_predicate;\n");
                        }
                        writer.write("    }\n");
                    }
                } else {
                    writer.write(s);
                }
            }
            writer.flush();
            writer.close();
            reader = new BufferedReader(new FileReader(SPIDEY_TOS_COMPONENT_TEMPLATE));
            writer = new BufferedWriter(new FileWriter(APP_WORKING_DIR + node.getId() + "/LNMessageC.nc"));
            while ((s = reader.readLine()) != null) {
                s = s.concat("\n");
                if (s.indexOf(COMPILER_TAG) != -1) {
                    writer.write(generateCompilerTag());
                } else if (s.indexOf(USER_INCLUDE) != -1) {
                    writer.write(Spidey.include);
                } else if (s.indexOf(SPIDEY_TOS_DATA) != -1) {
                    writer.write("  components Spidey_" + node.getId() + " as SpideyData;\n");
                } else if (s.indexOf(SPIDEY_USES) != -1) {
                    writer.write(Spidey.nesCuses);
                } else if (s.indexOf(SPIDEY_TOS_USES_WIRINGS) != -1) {
                    StringTokenizer tk = new StringTokenizer(Spidey.nesCuses, " ;\n");
                    while (tk.hasMoreTokens()) {
                        String token = tk.nextToken();
                        if (!token.equals("interface")) {
                            writer.write("  " + token + " = SpideyData;\n");
                        }
                    }
                } else {
                    writer.write(s);
                }
            }
            writer.flush();
            writer.close();
        }
    }
