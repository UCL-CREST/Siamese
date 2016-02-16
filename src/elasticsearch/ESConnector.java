package elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

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

	public ArrayList<String> search(String index, String type, String query, boolean isPrint, boolean isDFS) {
		ArrayList<String> results = new ArrayList<String>();
		SearchType searchType = SearchType.QUERY_THEN_FETCH;
		if (isDFS)
			searchType = SearchType.DFS_QUERY_THEN_FETCH;
		SearchResponse response = client.prepareSearch(index).setSearchType(searchType)
				.setQuery(QueryBuilders.matchQuery("src", query)).setFrom(0).setSize(10).execute()
				.actionGet();
		SearchHit[] hits = response.getHits().getHits();
		if (isPrint) System.out.println("=======================\nhits: " + hits.length);
		int count = 0;
		for (SearchHit hit : hits) {
			if (count >= 10)
				break;
			if (isPrint) System.out.println(hit.getId()); // prints out the id of the
			// document
			results.add(hit.getId());
			// Map<String, Object> result = hit.getSource(); // the retrieved
			// document
			count++;
		}
		if (isPrint) System.out.println("=======================");
		return results;
	}
}
