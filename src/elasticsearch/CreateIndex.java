/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package elasticsearch;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * A {@link Request} used to create indices.
 */
public class CreateIndex {

    private final CreateIndexRequest request;
    private final String index;

    public CreateIndex(String index) {
        this.index = index;
        request = new CreateIndexRequest(index);
    }

    public CreateIndex withSettings(Settings settings) {
        request.settings(settings);
        return this;
    }

    public CreateIndex withSettings(String source) {
        Settings settings = Settings.builder()
                .loadFromSource(source)
                .build();
        withSettings(settings);
        return this;
    }

    public CreateIndex withSource(String source) {
        request.source(source);
        return this;
    }

    public boolean execute(final Client client) throws ElasticsearchException {
		boolean status = false;
		try {
			CreateIndexResponse response = client.admin().indices().create(request).get();
			status = response.isAcknowledged();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return status;
    }

    @Override
    public String toString() {
        return "create index [" +
                "index='" + index + '\'' +
                ']';
    }
}