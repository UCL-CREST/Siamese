package elasticsearch.main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import elasticsearch.document.Document;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

public class ESConnector {
	private Client client;
	private String host;

	public ESConnector(String host) {
		this.host = host;
	}

	public void startup() throws UnknownHostException {
		org.elasticsearch.common.settings.Settings settings = org.elasticsearch.common.settings.Settings
				.settingsBuilder().put("cluster.name", "stackoverflow").build();
		// on startup
		client = TransportClient.builder().settings(settings).build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), 9300));
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
                        .field("file", d.getFile())
                        .field("src", d.getSource())
                        .endObject();

				// insert document one by one
				IndexResponse response = client.prepareIndex(index, type, d.getId()).setSource(builder).get();

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
	 boolean bulkInsert(String index, String type, ArrayList<Document> documents) {
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		// keep adding documents
		for (Document d : documents) {
		    // System.out.println(d.getId() + ", " + d.getFile());
			try {
				bulkRequest.add(client.prepareIndex(index, type, d.getId())
						.setSource(jsonBuilder().startObject()
                                .field("file", d.getFile())
                                .field("src", d.getSource())
                            .endObject()));

			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		// bulk insert once
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();

        return bulkResponse.hasFailures();
	}

    ArrayList<Document> search(String index, String type, String query, boolean isPrint
			, boolean isDFS, int resultOffset, int resultSize) {

        ArrayList<Document> results = new ArrayList<Document>();
        SearchType searchType;

        if (isDFS)
            searchType = SearchType.DFS_QUERY_THEN_FETCH;
        else
            searchType = SearchType.QUERY_THEN_FETCH;

        SearchResponse response = client.prepareSearch(index).setSearchType(searchType)
                .setQuery(QueryBuilders.matchQuery("src", query)).setFrom(resultOffset).setSize(resultSize).execute()
                .actionGet();
        SearchHit[] hits = response.getHits().getHits();

        int count = 0;
        for (SearchHit hit : hits) {

            if (count >= resultSize)
                break;

			// prints out the id of the document
            if (isPrint)
                System.out.println("ANS," + hit.getId() + "," + hit.getScore());

            Document d = new Document(hit.getId(),
                    hit.getSource().get("file").toString(),
                    hit.getSource().get("src").toString());
            results.add(d);

            count++;
        }

        return results;
    }

    boolean createIndex(String indexName, String typeName, String settingsStr, String mappingStr) {

		CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
		Settings settings = Settings.builder()
                .loadFromSource(settingsStr)
                .build();
		createIndexRequestBuilder.setSettings(settings);
		createIndexRequestBuilder.addMapping(typeName, mappingStr);
		CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();

		return response.isAcknowledged();
	}

    boolean deleteIndex(String indexName) {
		DeleteIndexRequest deleteRequest = new DeleteIndexRequest(indexName);
		DeleteIndexResponse response = client.admin().indices().delete(deleteRequest).actionGet();
		return response.isAcknowledged();
	}
	
    boolean isIndexExist(String indexName) {
		return client.admin().indices()
			    .prepareExists(indexName)
			    .execute().actionGet().isExists();
	}
	
    void refresh(String indexName) {
		client.admin().indices().prepareRefresh().execute().actionGet();
		client.admin().indices().prepareFlush(indexName).execute().actionGet();
	}
}
