    public static CodeBlock parse(BufferedReader reader) {
        Stack<CodeBlock> blockStack = new Stack<CodeBlock>();
        CodeBlock thisBlock = new CodeBlock();
        StringBuffer buffer = new StringBuffer();
        try {
            String nextLine = null;
            while ((nextLine = reader.readLine()) != null) {
                buffer.append(nextLine).append("\r\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(ARParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(buffer);
        int lastIndex = 0;
        while (matcher.find()) {
            if (matcher.start() - lastIndex > 1) {
                thisBlock.add(new StaticCodeBlock(buffer.substring(lastIndex, matcher.start())));
                lastIndex = matcher.end();
            }
            if (matcher.group(ECHO) != null) {
                thisBlock.add(new EchoCodeBlock(matcher.group(ECHO)));
            } else if (matcher.group(IF_IDX) != null) {
                IfCodeBlock ifBlock = new IfCodeBlock();
                IfConditionBlock ifCondition = new IfConditionBlock(matcher.group(IF_VAR));
                ifBlock.addCondition(ifCondition);
                thisBlock.add(ifBlock);
                blockStack.push(thisBlock);
                blockStack.push(ifBlock);
                thisBlock = ifCondition.getBlock();
            } else if (matcher.group(FOR_IDX) != null) {
                ForCodeBlock forBlock = new ForCodeBlock(matcher.group(FOR_ELEM), matcher.group(FOR_ARR));
                thisBlock.add(forBlock);
                blockStack.push(thisBlock);
                thisBlock = forBlock;
            } else if (matcher.group(ELSEIF) != null) {
                if (blockStack.isEmpty()) {
                    throw new RuntimeException("Invalid template");
                }
                thisBlock = blockStack.pop();
                if (thisBlock instanceof IfCodeBlock) {
                    IfConditionBlock ifCondition = new IfConditionBlock(matcher.group(ELSEIF_VAR));
                    ((IfCodeBlock) thisBlock).addCondition(ifCondition);
                    blockStack.push(thisBlock);
                    thisBlock = ifCondition.getBlock();
                } else {
                    throw new RuntimeException("invalid elseif statement");
                }
            } else if (matcher.group(ELSE) != null) {
                if (blockStack.isEmpty()) {
                    throw new RuntimeException("Invalid template");
                }
                thisBlock = blockStack.pop();
                if (thisBlock instanceof IfCodeBlock) {
                    IfConditionBlock ifCondition = new IfConditionBlock();
                    ((IfCodeBlock) thisBlock).addCondition(ifCondition);
                    blockStack.push(thisBlock);
                    thisBlock = ifCondition.getBlock();
                } else {
                    throw new RuntimeException("invalid elseif statement");
                }
            } else if (matcher.group(ENDFOR) != null) {
                if (blockStack.isEmpty()) {
                    throw new RuntimeException("Invalid template");
                }
                thisBlock = blockStack.pop();
            } else if (matcher.group(ENDIF) != null) {
                if (blockStack.size() < 2) {
                    throw new RuntimeException("Invalid template");
                }
                thisBlock = blockStack.pop();
                thisBlock = blockStack.pop();
            }
        }
        if (lastIndex < buffer.length()) {
            thisBlock.add(new StaticCodeBlock(buffer.substring(lastIndex, buffer.length())));
        }
        return thisBlock;
    }
