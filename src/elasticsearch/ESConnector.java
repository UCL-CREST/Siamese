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
		// on startup
		client = TransportClient.builder().build()
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), 9300));
	}

	public void shutdown() {
		client.close();
	}

	public ArrayList<String> search(String query) {
		ArrayList<String> results = new ArrayList<String>();
		SearchResponse response = client.prepareSearch("cloplag").setSearchType(SearchType.QUERY_AND_FETCH)
				.setQuery(QueryBuilders.matchQuery("src", query)).setFrom(0).setSize(10).setExplain(true).execute()
				.actionGet();
		SearchHit[] hits = response.getHits().getHits();
		// System.out.println("hits: " + hits.length);
		int count = 0;
		for (SearchHit hit : hits) {
			count++;
			if (count >= 10) break;
			// System.out.println(hit.getId()); // prints out the id of the document
			results.add(hit.getId());
			// Map<String, Object> result = hit.getSource(); // the retrieved document
		}
		return results;
	}
}
