    void process(SourceMetricsDataBuilder data) {
        data.set(SourceMetricsData.SIZE, size);
        Set<String> operators = new HashSet<String>();
        int standAlone = 0;
        for (SourceTokens.Token token : sourceTokens) {
            if (token.isEOF()) {
                SourceTokens.Token prev = token.previousToken();
                if (null == prev) {
                    data.set(SourceMetricsData.LINES, 0);
                } else if (prev.getTokenText().endsWith("\r") || prev.getTokenText().endsWith("\n")) {
                    data.set(SourceMetricsData.LINES, token.getLineNumber() - 1L);
                } else {
                    data.set(SourceMetricsData.LINES, token.getLineNumber());
                }
                break;
            }
            if (token.isLineComment()) {
                if (isFirstOnLine(token)) {
                    data.bump(SourceMetricsData.LINE_COMMENT);
                    if (endOfMultipleLineComments(token)) {
                        bumpCommentLocationMetrics(data, token);
                    }
                }
            } else if (token.isBlockComment()) {
                processMultilineComment(data, token, SourceMetricsData.BLOCK_COMMENT);
            } else if (token.isJavadocComment()) {
                processMultilineComment(data, token, SourceMetricsData.DOC_COMMENT);
            } else if (token.isWhitespace()) {
                int lineCount = token.getNewlineCount();
                SourceTokens.Token prev = token.previousToken();
                if ((null != prev) && prev.isLineComment()) {
                    lineCount++;
                }
                if (1 < lineCount) {
                    data.bump(SourceMetricsData.WHITESPACE, lineCount - 1L);
                }
            } else if (";".equals(token.getTokenText())) {
                data.bump(SourceMetricsData.LOGICAL_LINES);
            } else if (-1 != "(){}".indexOf(token.getTokenText())) {
                if (isAloneOnLine(token)) {
                    standAlone++;
                }
            }
            if (token.isLiteral()) {
                data.bump(SourceMetricsData.OPERANDS);
            } else if (!token.isComment() && !token.isWhitespace()) {
                data.bump(SourceMetricsData.OPERATORS);
                operators.add(token.getTokenText());
            }
        }
        data.set(SourceMetricsData.UNIQUE_OPERATORS, operators.size());
        data.set(SourceMetricsData.LOC, data.get(SourceMetricsData.LINES) - data.get(SourceMetricsData.WHITESPACE) - data.get(SourceMetricsData.LINE_COMMENT) - data.get(SourceMetricsData.BLOCK_COMMENT) - data.get(SourceMetricsData.DOC_COMMENT));
        data.set(SourceMetricsData.ELOC, data.get(SourceMetricsData.LOC) - standAlone);
        data.set(SourceMetricsData.COMMENTS, data.get(SourceMetricsData.LINE_COMMENT) + data.get(SourceMetricsData.BLOCK_COMMENT) + data.get(SourceMetricsData.DOC_COMMENT));
        String chunk = new String(sourceTokens.getChunk());
        byte[] bytes = chunk.getBytes();
        CRC32 crc = new CRC32();
        crc.update(bytes);
        data.set(SourceMetricsData.CRC, Double.longBitsToDouble(crc.getValue()));
    }
