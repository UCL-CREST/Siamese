package elasticsearch.main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
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
                // System.out.println(d.getId() + ", " + d.getFile());

			    XContentBuilder builder = jsonBuilder()
                        .startObject()
                        .field("file", d.getFile())
                        .field("src", d.getSource())
                        .endObject();

				// insert document one by one
				IndexResponse response = client.prepareIndex(index, type, d.getId()).setSource(builder).get();
				isCreated = response.isCreated();

				if (!isCreated) {
					throw new Exception("cannot insert " + d.getId() + ", " + d.getFile()
							+ ", src = " + builder.string());
				} else {
				     // System.out.println("inserted: " + d.getId());
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

        // check for failures after insertion
		boolean hasFailure = false;

        hasFailure = bulkResponse.hasFailures();

        if (hasFailure)
			return false;
		else
			return true;
	}

	public ArrayList<String> search(String index, String type, String query, boolean isPrint, boolean isDFS) {
		ArrayList<String> results = new ArrayList<String>();
		SearchType searchType;
		if (isDFS)
			searchType = SearchType.DFS_QUERY_THEN_FETCH;
		else
			searchType = SearchType.QUERY_THEN_FETCH;
		
		SearchResponse response = client.prepareSearch(index).setSearchType(searchType)
				.setQuery(QueryBuilders.matchQuery("src", query)).setFrom(0).setSize(10).execute()
				.actionGet();
		SearchHit[] hits = response.getHits().getHits();
		// if (isPrint) System.out.println("=======================\nhits: " + hits.length);
		int count = 0;
		for (SearchHit hit : hits) {
			if (count >= 10)
				break;
			if (isPrint) System.out.println("ANS," + hit.getId() + "," + hit.getScore()); // prints out the id of the
			// document
			results.add(hit.getId());
			// Map<String, Object> result = hit.getSource(); // the retrieved
			// document
			count++;
		}
		// if (isPrint) System.out.println("=======================");
		// if (isPrint) System.out.println();
		return results;
	}
	
	public boolean createIndex(String indexName, String typeName, String settingsStr, String mappingStr) {
		CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
		Settings settings = Settings.builder()
                .loadFromSource(settingsStr)
                .build();
		createIndexRequestBuilder.setSettings(settings);
		// System.out.println("Type name = " + typeName);
		createIndexRequestBuilder.addMapping(typeName, mappingStr);
		//System.out.println(mappingStr);
        // createIndexRequestBuilder.addMapping(type, mappingStr);
		
		CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
		return response.isAcknowledged();
	}

	public boolean deleteIndex(String indexName) {
		DeleteIndexRequest deleteRequest = new DeleteIndexRequest(indexName);
		DeleteIndexResponse response = client.admin().indices().delete(deleteRequest).actionGet();
		return response.isAcknowledged();
	}
	
	public boolean isIndexExist(String indexName) {
		boolean exists = client.admin().indices()
			    .prepareExists(indexName)
			    .execute().actionGet().isExists();
		return exists;
	}
	
	public void refresh() {
		client.admin().indices().prepareRefresh().execute().actionGet();
	}
}
