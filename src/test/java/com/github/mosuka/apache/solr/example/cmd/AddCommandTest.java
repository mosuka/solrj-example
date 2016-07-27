package com.github.mosuka.apache.solr.example.cmd;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.util.LuceneTestCase.Slow;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.cloud.AbstractDistribZkTestBase;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.junit.BeforeClass;
import org.junit.Test;

@Slow
public class AddCommandTest extends SolrCloudTestCase {

  private static final String COLLECTION = "collection1";
  private static final String CONF_NAME = COLLECTION + "_config";
  private static final String CONF_DIR = "src/test/resources/solr/" + COLLECTION + "/conf";
  private static final int NUM_NODES = 2;
  private static final int NUM_SHARDS = 2;
  private static final int NUM_REPLICAS = 1;
  private static final int TIMEOUT = 30;

  @BeforeClass
  public static void setupCluster() throws Exception {
    configureCluster(NUM_NODES).addConfig(CONF_NAME, getFile(CONF_DIR).toPath()).configure();

    CollectionAdminRequest.createCollection(COLLECTION, CONF_NAME, NUM_SHARDS, NUM_REPLICAS)
        .process(cluster.getSolrClient());
    AbstractDistribZkTestBase.waitForRecoveriesToFinish(COLLECTION,
        cluster.getSolrClient().getZkStateReader(), false, true, TIMEOUT);
  }

  @Test
  public void testExecute() throws SolrServerException, IOException {
    String solrUrl = cluster.getJettySolrRunners().get(0).getBaseUrl() + "/" + COLLECTION;

    Map<String, Object> attrs = new HashMap<String, Object>();
    attrs.put("solr_url", solrUrl);
    attrs.put("data", "{\"id\":\"1\",\"title\":\"SolrJ\"}");

    AddCommand addCommand = new AddCommand();
    addCommand.execute(attrs);

    Map<String, Object> sAttrs = new HashMap<String, Object>();
    sAttrs.put("solr_url", solrUrl);
    sAttrs.put("query", "title:SolrJ");

    SearchCommand searchCommand = new SearchCommand();
    searchCommand.execute(sAttrs);

    assertEquals("aaa", "aaa");
  }

}
