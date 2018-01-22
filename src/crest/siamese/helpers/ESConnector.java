package crest.siamese.helpers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

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
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class ESConnector {
	private Client client;
	private String host;

	public ESConnector(String host) {
		this.host = host;
	}

    public Client startup() throws UnknownHostException {
		org.elasticsearch.common.settings.Settings settings = org.elasticsearch.common.settings.Settings
				.settingsBuilder().put("cluster.name", "stackoverflow").build();
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
				.setQuery(QueryBuilders.matchQuery("src", query)).setFrom(resultOffset).setSize(resultSize).execute()
				.actionGet();
		SearchHit[] hits = response.getHits().getHits();

        return prepareResults(hits, resultSize, isPrint);
    }

	public ArrayList<ArrayList<Document>> multiSearch(String index, String type, ArrayList<String> origQuery, boolean isPrint
			, boolean isDFS, int resultOffset, int resultSize) throws Exception {

		ArrayList<ArrayList<Document>> results = new ArrayList<>();
		SearchType searchType;
		MultiSearchRequestBuilder msrb = client.prepareMultiSearch();

		if (isDFS)
			searchType = SearchType.DFS_QUERY_THEN_FETCH;
		else
			searchType = SearchType.QUERY_THEN_FETCH;

		for (int i=0; i < origQuery.size(); i++) {
			/*
			copied from
			https://stackoverflow.com/questions/43394976/can-i-search-by-multiple-fields-using-the-elastic-search-java-api
			*/
			QueryBuilder queryBuilder = QueryBuilders.matchQuery("src", origQuery.get(i));
			SearchRequestBuilder srBuilder = client.prepareSearch()
					.setSearchType(searchType)
					.addSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
					.addSort(SortBuilders.fieldSort("file").order(SortOrder.DESC))
					.setQuery(queryBuilder)
					.setFrom(resultOffset)
					.setSize(resultSize);
			msrb.add(srBuilder);
		}

		MultiSearchResponse msResponses = msrb.execute().actionGet();
		for (MultiSearchResponse.Item item : msResponses.getResponses()) {
			SearchResponse response = item.getResponse();
			SearchHit[] hits = response.getHits().getHits();
			ArrayList<Document> docResults = prepareResults(hits, resultSize, isPrint);
			results.add(docResults);
		}

		return results;
	}

    public ArrayList<Document> search(
            String index,
            String type,
            String origQuery,
            String query,
            int origBoost,
            int normBoost,
            boolean isPrint,
            boolean isDFS,
            int resultOffset,
            int resultSize) throws Exception {

        ArrayList<Document> results = new ArrayList<Document>();
        SearchType searchType;

        if (isDFS)
            searchType = SearchType.DFS_QUERY_THEN_FETCH;
        else
            searchType = SearchType.QUERY_THEN_FETCH;

        /* copied from
        https://stackoverflow.com/questions/43394976/can-i-search-by-multiple-fields-using-the-elastic-search-java-api
         */
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .should(
                        QueryBuilders.matchQuery("tokenizedsrc", origQuery)
                                .boost(origBoost)
                )
                .should(
                        QueryBuilders.matchQuery("src", query)
//                                .operator(MatchQueryBuilder.Operator.AND)
                                .boost(normBoost)
                );

        SearchResponse response = client.prepareSearch(index).setSearchType(searchType)
                .addSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
                .addSort(SortBuilders.fieldSort("file").order(SortOrder.DESC))
                .setQuery(queryBuilder)
                .setFrom(resultOffset).setSize(resultSize).execute()
                .actionGet();
        SearchHit[] hits = response.getHits().getHits();

        return prepareResults(hits, resultSize, isPrint);
    }

	public ArrayList<ArrayList<Document>> multiSearch(
			String index,
			String type,
			ArrayList<String> origQuery,
			ArrayList<String> query,
			int origBoost,
			int normBoost,
			boolean isPrint,
			boolean isDFS,
			int resultOffset,
			int resultSize) throws Exception {


		ArrayList<ArrayList<Document>> results = new ArrayList<>();
		SearchType searchType;
		MultiSearchRequestBuilder msrb = client.prepareMultiSearch();

		if (isDFS)
			searchType = SearchType.DFS_QUERY_THEN_FETCH;
		else
			searchType = SearchType.QUERY_THEN_FETCH;

		for (int i=0; i < origQuery.size(); i++) {
			/*
			copied from
			https://stackoverflow.com/questions/43394976/can-i-search-by-multiple-fields-using-the-elastic-search-java-api
			*/
			QueryBuilder queryBuilder = QueryBuilders.boolQuery()
					.should(
							QueryBuilders.matchQuery("tokenizedsrc", origQuery.get(i))
									.boost(origBoost)
					)
					.should(
							QueryBuilders.matchQuery("src", query.get(i))
									.boost(normBoost)
					);
			SearchRequestBuilder srBuilder = client.prepareSearch()
					.setSearchType(searchType)
					.addSort(SortBuilders.fieldSort("_score").order(SortOrder.DESC))
//					.addSort(SortBuilders.fieldSort("file").order(SortOrder.DESC))
					.setQuery(queryBuilder)
					.setFrom(resultOffset)
					.setSize(resultSize);
			msrb.add(srBuilder);
		}

		MultiSearchResponse msResponses = msrb.execute().actionGet();

		// You will get all individual responses from MultiSearchResponse#getResponses()
		for (MultiSearchResponse.Item item : msResponses.getResponses()) {
			SearchResponse response = item.getResponse();
			SearchHit[] hits = response.getHits().getHits();
			ArrayList<Document> docResults = prepareResults(hits, resultSize, isPrint);
			results.add(docResults);
		}

		return results;
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
