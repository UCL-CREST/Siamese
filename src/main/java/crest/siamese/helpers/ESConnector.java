/*
   Copyright 2018 Chaiyong Ragkhitwetsagul and Jens Krinke

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package crest.siamese.helpers;

import com.google.gson.JsonParser;
import crest.siamese.document.Document;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ESConnector {
	private Client client;
	private String host;
	private String cluster;

	public ESConnector(String host, String cluster) {
		this.host = host;
		this.cluster = cluster;
	}

    public Client startup() throws UnknownHostException {
		org.elasticsearch.common.settings.Settings settings = org.elasticsearch.common.settings.Settings
				.settingsBuilder().put("cluster.name", cluster).build();
		// on startup
		client = TransportClient.builder().settings(settings).build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), 9300));

		return client;
	}

	public void shutdown() {
		client.close();
	}
	
	/***
	 * A method for one-by-one document indexing
	 * @param index the index name
	 * @param type the doc type name
	 * @param documents the array of documents
	 * @return status of bulk insert (true = no failure, false = failures)
	 */
	public boolean sequentialInsert(String index, String type, ArrayList<Document> documents) throws Exception {

	    boolean isCreated = false;

		for (Document d : documents) {
			try {
			    XContentBuilder builder = jsonBuilder()
                        .startObject()
						.field("id", d.getId())
                        .field("file", d.getFile())
                        .field("startline", d.getStartLine())
                        .field("endline", d.getEndLine())
                        .field("src", d.getSource())
						.field("t2src", d.getT2Source())
						.field("t1src", d.getT1Source())
                        .field("tokenizedsrc", d.getTokenizedSource())
                        .field("origsrc", d.getOriginalSource())
                        .field("license", d.getLicense())
                        .field("url", d.getUrl())
                        .endObject();

				// insert document one by one
				IndexResponse response = client.prepareIndex(index, type).setSource(builder).get();

				if (!response.isCreated()) {
					throw new Exception("cannot insert " + d.getId() + ", " + d.getFile()
							+ ", src = " + builder.string());
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/***
	 * A method for bulk insertion of documents
	 * @param index the index name
	 * @param type the doc type name
	 * @param documents array of documents
	 * @return status of bulk insert (true = no failure, false = failures)
	 */
    public boolean bulkInsert(String index, String type, ArrayList<Document> documents) {

		BulkRequestBuilder bulkRequest = client.prepareBulk();

		// keep adding documents
		for (Document d : documents) {
			try {
				bulkRequest.add(client.prepareIndex(index, type)
						.setSource(jsonBuilder().startObject()
								.field("id", d.getId())
								.field("file", d.getFile())
								.field("startline", d.getStartLine())
								.field("endline", d.getEndLine())
								.field("src", d.getSource())
								.field("t2src", d.getT2Source())
								.field("t1src", d.getT1Source())
                                .field("tokenizedsrc", d.getTokenizedSource())
								.field("origsrc", d.getOriginalSource())
								.field("license", d.getLicense())
								.field("url", d.getUrl())
                            .endObject()));
			} catch (IOException e) {
//				e.printStackTrace();
				System.out.println(e.getMessage());
				return false;
			}
		}

		// bulk insert once
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            for (BulkItemResponse brsp : bulkResponse) {
                if (brsp.isFailed()) {
                    System.out.println("Failed to index (message: " + brsp.getFailureMessage() + ")");
                }
            }
        }

        return !bulkResponse.hasFailures();
	}

	public long getMaxId(String index, boolean isDFS) throws Exception {
		SearchType searchType;

		if (isDFS)
			searchType = SearchType.DFS_QUERY_THEN_FETCH;
		else
			searchType = SearchType.QUERY_THEN_FETCH;

		SearchResponse response = client.prepareSearch(index).setSearchType(searchType)
				.addSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
				.addSort(SortBuilders.fieldSort("file").order(SortOrder.DESC))
				.setQuery(QueryBuilders.matchAllQuery())
				.addAggregation(AggregationBuilders.max("max_id").field("id"))
				.execute()
				.actionGet();

		Double maxId = (Double) response.getAggregations().get("max_id").getProperty("value");

		return maxId.longValue();
	}

	public String delete(String index, String type, String field, String query, boolean isDFS, int amount) {
		SearchType searchType;
		String output = "";
		if (isDFS)
			searchType = SearchType.DFS_QUERY_THEN_FETCH;
		else
			searchType = SearchType.QUERY_THEN_FETCH;

		SearchResponse response = client.prepareSearch(index).setSearchType(searchType)
				.addSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
				.addSort(SortBuilders.fieldSort("file").order(SortOrder.DESC))
				.setScroll(new TimeValue(60000))
				.setSize(amount)
				.setQuery(QueryBuilders.wildcardQuery(field, query)).execute()
				.actionGet();

		// Scroll until no hits are returned
		while (true) {

			SearchHit[] hits = response.getHits().getHits();

			BulkRequestBuilder bulkRequest = client.prepareBulk();
			Arrays.asList(hits).stream().forEach(h ->
					bulkRequest.add(client.prepareDelete()
							.setIndex(index)
							.setType(type)
							.setId(h.getId())));

			BulkResponse bulkResponse = bulkRequest.execute().actionGet();

			if (bulkResponse.hasFailures()) {
				throw new RuntimeException(bulkResponse.buildFailureMessage());
			} else {
				output += "Deleted " + hits.length + " docs in " + bulkResponse.getTook() + "\n";
			}

			response = client.prepareSearchScroll(response.getScrollId())
					.setScroll(new TimeValue(60000)).execute().actionGet();
			//Break condition: No hits are returned
			if (response.getHits().getHits().length == 0) {
				break;
			}
		}

		return output;
	}

    public ArrayList<Document> search(String index, String type, String query, boolean isPrint
			, boolean isDFS, int resultOffset, int resultSize) throws Exception {
        SearchType searchType;

        if (isDFS)
            searchType = SearchType.DFS_QUERY_THEN_FETCH;
        else
            searchType = SearchType.QUERY_THEN_FETCH;

		SearchResponse response = client.prepareSearch(index).setSearchType(searchType)
				.addSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
				.addSort(SortBuilders.fieldSort("file").order(SortOrder.DESC))
				.setQuery(QueryBuilders.matchQuery("tokenizedsrc", query))
//				.setQuery(QueryBuilders.matchQuery("src", query))
				.setFrom(resultOffset).setSize(resultSize).execute()
				.actionGet();
		SearchHit[] hits = response.getHits().getHits();

        return prepareResults(hits, resultSize, isPrint);
    }

    private QueryBuilder getQueryBuilder(String origQuery, int origBoost, String t2Query, int t2Boost,
										 String t1Query, int t1Boost, String query, int normBoost,
										 String[] similarity) {
    	        /* copied from
        https://stackoverflow.com/questions/43394976/can-i-search-by-multiple-fields-using-the-elastic-search-java-api
         */
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.should(
//						QueryBuilders.commonTermsQuery("tokenizedsrc", origQuery)
//								.cutoffFrequency(cutoff2)
//								.boost(origBoost)
						QueryBuilders.matchQuery("tokenizedsrc", origQuery)
								.minimumShouldMatch(similarity[0])
								.boost(origBoost)
				)
				.should(
//						QueryBuilders.commonTermsQuery("tokenizedsrc", origQuery)
//								.cutoffFrequency(cutoff2)
//								.boost(origBoost)
						QueryBuilders.matchQuery("t2src", t2Query)
								.minimumShouldMatch(similarity[2])
								.boost(t2Boost)
				)
				.should(
//						QueryBuilders.commonTermsQuery("tokenizedsrc", origQuery)
//								.cutoffFrequency(cutoff2)
//								.boost(origBoost)
						QueryBuilders.matchQuery("t1src", t1Query)
								.minimumShouldMatch(similarity[1])
								.boost(t1Boost)
				)
				.should(
//                		QueryBuilders.commonTermsQuery("src", query)
//								.cutoffFrequency(cutoff)
//								.boost(normBoost)
						QueryBuilders.matchQuery("src", query)
//                                .operator(MatchQueryBuilder.Operator.AND)
								.minimumShouldMatch(similarity[3])
								.boost(normBoost)
				).minimumShouldMatch("4"); // number of representations that must match at the given similarity
		return queryBuilder;
	}

	private QueryBuilder getQueryBuilder(String origQuery, int origBoost, String t2Query, int t2Boost,
										 String t1Query, int t1Boost, String query, int normBoost) {
    	/* copied from
        https://stackoverflow.com/questions/43394976/can-i-search-by-multiple-fields-using-the-elastic-search-java-api
         */
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.should(
						QueryBuilders.matchQuery("tokenizedsrc", origQuery)
								.boost(origBoost)
				)
				.should(
						QueryBuilders.matchQuery("t2src", t2Query)
								.boost(t2Boost)
				)
				.should(
						QueryBuilders.matchQuery("t1src", t1Query)
								.boost(t1Boost)
				)
				.should(
						QueryBuilders.matchQuery("src", query)
								.boost(normBoost)
				);
		return queryBuilder;
	}

    public ArrayList<Document> search(
            String index,
            String type,
            String origQuery,
            String query,
            String t2Query,
			String t1Query,
            int origBoost,
            int normBoost,
            int t2Boost,
			int t1Boost,
            boolean isPrint,
            boolean isDFS,
            int resultOffset,
            int resultSize,
			String computeSimilarity,
			String[] similarity) throws Exception {
        SearchType searchType;
        if (isDFS)
            searchType = SearchType.DFS_QUERY_THEN_FETCH;
        else
            searchType = SearchType.QUERY_THEN_FETCH;
		QueryBuilder queryBuilder;
		if (computeSimilarity.equals("none") || computeSimilarity.equals("fuzzywuzzy"))
			queryBuilder = getQueryBuilder(origQuery, origBoost, t2Query, t2Boost,
					t1Query, t1Boost, query, normBoost);
		else if (computeSimilarity.equals("tokenratio"))
			queryBuilder = getQueryBuilder(origQuery, origBoost, t2Query, t2Boost,
					t1Query, t1Boost, query, normBoost, similarity);
		else
			throw new Exception("ERROR: wrong similarity measure.");
        SearchResponse response = client.prepareSearch(index).setSearchType(searchType)
                .addSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
                .addSort(SortBuilders.fieldSort("file").order(SortOrder.DESC))
                .setQuery(queryBuilder)
                .setFrom(resultOffset).setSize(resultSize).execute()
                .actionGet();
        SearchHit[] hits = response.getHits().getHits();

        return prepareResults(hits, resultSize, isPrint);
    }

    public long getIndicesStats(String indexName) {
        IndicesStatsResponse indicesStatsResponse = client.admin().indices().prepareStats(indexName)
                .all()
                .execute().actionGet();

        XContentBuilder builder = null;
        try {
            builder = XContentFactory.jsonBuilder();
            builder.startObject();
            indicesStatsResponse.toXContent(builder, ToXContent.EMPTY_PARAMS);
            builder.endObject();
            String jsonResponse = builder.prettyPrint().string();

            JsonParser jsonParser = new JsonParser(); // from import com.google.gson.JsonParser;
            Long docCount = jsonParser.parse(jsonResponse)
                    .getAsJsonObject().get("_all")
                    .getAsJsonObject().get("primaries")
                    .getAsJsonObject().get("docs")
                    .getAsJsonObject().get("count").getAsLong();
//            System.out.println(docCount);
            return docCount;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private ArrayList<Document> prepareResults(SearchHit[] hits, int resultSize, boolean isPrint) throws Exception {
        ArrayList<Document> results = new ArrayList<Document>();
        int count = 0;
        for (SearchHit hit : hits) {

            if (count >= resultSize)
                break;

            // prints out the id of the document
            if (isPrint)
                System.out.println("ANS," + hit.getId() + "," + hit.getScore());

            try {
                Document d = new Document(
						Long.parseLong(hit.getSource().get("id").toString()),
                        hit.getSource().get("file").toString(),
                        Integer.parseInt(hit.getSource().get("startline").toString()),
                        Integer.parseInt(hit.getSource().get("endline").toString()),
                        hit.getSource().get("src").toString(),
						hit.getSource().get("t2src").toString(),
						hit.getSource().get("t1src").toString(),
                        hit.getSource().get("tokenizedsrc").toString(),
                        hit.getSource().get("origsrc").toString(),
                        hit.getSource().get("license").toString(),
                        hit.getSource().get("url").toString());
                results.add(d);

                count++;
            } catch (NullPointerException e) {
                throw new Exception("ERROR: Query failed because of null value(s).");
            }
        }

        return results;
    }

    public boolean createIndex(String indexName, String typeName, String settingsStr, String mappingStr) {

		CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
		Settings settings = Settings.builder()
                .loadFromSource(settingsStr)
                .build();
		createIndexRequestBuilder.setSettings(settings);
		createIndexRequestBuilder.addMapping(typeName, mappingStr);
		CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();

		return response.isAcknowledged();
	}

    public boolean deleteIndex(String indexName) {
		DeleteIndexRequest deleteRequest = new DeleteIndexRequest(indexName);
		DeleteIndexResponse response = client.admin().indices().delete(deleteRequest).actionGet();
		return response.isAcknowledged();
	}

    public boolean doesIndexExist(String indexName) throws NoNodeAvailableException {
        try {
             boolean status = client.admin().indices()
                    .prepareExists(indexName)
                    .execute().actionGet().isExists();
             return status;
        } catch (NoNodeAvailableException e) {
            throw e;
        }
	}

    public void refresh(String indexName) {
		client.admin().indices().prepareRefresh().execute().actionGet();
		client.admin().indices().prepareFlush(indexName).execute().actionGet();
	}
}
