package com.github.mosuka.apache.solr.example.cmd;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.util.LuceneTestCase.Slow;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.cloud.AbstractDistribZkTestBase;
import org.apache.solr.cloud.SolrCloudTestCase;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.BeforeClass;
import org.junit.Test;

@Slow
public class SearchCommandTest extends SolrCloudTestCase {
  private ByteArrayOutputStream _baos;
  private PrintStream _out;

  private static final String COLLECTION = "collection1";
  private static final String CONF_NAME = COLLECTION + "_config";
  private static final String CONF_DIR = "src/test/resources/solr/" + COLLECTION + "/conf";
  private static final int NUM_NODES = 1;
  private static final int NUM_SHARDS = 1;
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

  @Override
  public void setUp() throws Exception {
    super.setUp();

    String solrUrl = cluster.getJettySolrRunners().get(0).getBaseUrl() + "/" + COLLECTION;
    Map<String, Object> attrs = new HashMap<String, Object>();
    attrs.put("solr_url", solrUrl);
    attrs.put("data",
        "{\"id\":\"1\",\"title_txt_en\":\"SolrJ\",\"description_txt_en\":\"SolrJ is a library for Solr.\"}");
    AddCommand addCommand = new AddCommand();
    addCommand.execute(attrs);

    _baos = new ByteArrayOutputStream();
    _out = System.out;
    System.setOut(new PrintStream(new BufferedOutputStream(_baos)));
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();

    System.setOut(_out);
  }

  @Test
  public void testExecute() throws SolrServerException, IOException {
    String solrUrl = cluster.getJettySolrRunners().get(0).getBaseUrl() + "/" + COLLECTION;

    Map<String, Object> attrs = new HashMap<String, Object>();
    attrs.put("solr_url", solrUrl);
    attrs.put("query", "SolrJ");

    SearchCommand searchCommand = new SearchCommand();
    searchCommand.execute(attrs);

    System.out.flush();

    String expected = "{\"status\":0,\"message\":\"OK\"}\n";
    String actual = _baos.toString();

    Map<String, Object> expectedMap =
        new ObjectMapper().readValue(expected, new TypeReference<HashMap<String, Object>>() {
        });

    Map<String, Object> actualMap =
        new ObjectMapper().readValue(actual, new TypeReference<HashMap<String, Object>>() {
        });

    assertEquals(expectedMap.get("status"), actualMap.get("status"));
  }

  @Test
  public void testExecuteSolrCloud() throws SolrServerException, IOException {
    String zooKeeperHost = cluster.getZkServer().getZkHost();
    String zooKeeperChroot = "/solr";
    String collection = COLLECTION;

    Map<String, Object> attrs = new HashMap<String, Object>();
    attrs.put("zookeeper_host", zooKeeperHost);
    attrs.put("zookeeper_chroot", zooKeeperChroot);
    attrs.put("collection", collection);
    attrs.put("query", "SolrJ");

    SearchCommand searchCommand = new SearchCommand();
    searchCommand.execute(attrs);

    System.out.flush();

    String expected = "{\"status\":0,\"message\":\"OK\"}\n";
    String actual = _baos.toString();

    Map<String, Object> expectedMap =
        new ObjectMapper().readValue(expected, new TypeReference<HashMap<String, Object>>() {
        });

    Map<String, Object> actualMap =
        new ObjectMapper().readValue(actual, new TypeReference<HashMap<String, Object>>() {
        });

    assertEquals(expectedMap.get("status"), actualMap.get("status"));
  }

}
