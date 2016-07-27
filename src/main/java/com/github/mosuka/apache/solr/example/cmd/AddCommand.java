package com.github.mosuka.apache.solr.example.cmd;

import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import com.github.mosuka.apache.solr.example.utils.SolrJExampleUtil;

public class AddCommand implements Command {

  @Override
  public void execute(Map<String, Object> attrs) {
    String responseJSON = null;

    try {
      SolrClient solrClient = null;
      try {
        String solrUrl = (String) attrs.get("solr_url");
        solrClient = new HttpSolrClient.Builder(solrUrl).build();

        SolrInputDocument document = SolrJExampleUtil.createDocument((String) attrs.get("data"));

        UpdateResponse response = solrClient.add(document);
        if (response.getStatus() == 0) {
          response = solrClient.commit();
        }

        responseJSON = String.format("{\"status\":%d}", response.getStatus());
      } catch (IOException e) {
        responseJSON = String.format("{\"status\":1, \"message\":\"%s\"}", e.getMessage());
      } catch (SolrServerException e) {
        responseJSON = String.format("{\"status\":1, \"message\":\"%s\"}", e.getMessage());
      } finally {
        if (solrClient != null) {
          solrClient.close();
        }
      }
    } catch (IOException e) {
      responseJSON = String.format("{\"status\":1, \"message\":\"%s\"}", e.getMessage());
    }

    System.out.println(responseJSON);
  }

}
