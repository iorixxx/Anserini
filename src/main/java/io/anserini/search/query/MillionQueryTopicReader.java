package io.anserini.search.query;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * TREC 2009 Million Query (1MQ) Track
 * <p>
 * http://ir.cis.udel.edu/million/data.html
 */
public class MillionQueryTopicReader extends TopicReader {

    public MillionQueryTopicReader(Path topicFile) {
        super(topicFile);
    }

    /**
     * P is the priority (a number 1-4, with 1 indicating highest priority)
     */
    static final int P = 1;

    protected void checkQueryIDRange(int queryID) {
        if (queryID < 20001 || queryID > 60000)
            throw new IllegalArgumentException("queryID: " + queryID + " must be between 20001 and 60000");
    }

    @Override
    public SortedMap<Integer, String> read() throws IOException {

        SortedMap<Integer, String> map = new TreeMap<>();

        List<String> lines = Files.readAllLines(topicFile, StandardCharsets.ISO_8859_1);

        for (String line : lines) {
            String parts[] = line.split(":");
            assert parts.length == 3 : "topic does not contain colon : " + line;

            int qID = Integer.parseInt(parts[0]);
            checkQueryIDRange(qID);
            // P is the priority (a number 1-4, with 1 indicating highest priority)
            int priority = Integer.parseInt(parts[1]);
            if (priority > P) break;

            String query = parts[2].trim();

            map.put(qID, query);
        }
        lines.clear();

        return map;
    }
}
