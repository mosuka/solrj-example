package com.github.mosuka.apache.solr.example.cmd;

import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;

public class DeleteCommand implements Command {

  @Override
  public void execute(Map<String, Object> attrs) {
    String responseJSON = null;

    try {
      SolrClient solrClient = null;
      try {
        solrClient =
            new HttpSolrClient.Builder((String) attrs.get("solr_url")).build();

        UpdateResponse response =
            solrClient.deleteById((String) attrs.get("unique_key_field_value"));

        solrClient.commit();

        responseJSON = String.format("{\"status\":%d}", response.getStatus());
      } catch (IOException e) {
        responseJSON = String.format("{\"status\":\"NG\", \"message\":\"%s\"}",
            e.getMessage());
      } catch (SolrServerException e) {
        responseJSON = String.format("{\"status\":\"NG\", \"message\":\"%s\"}",
            e.getMessage());
      } finally {
        solrClient.close();
      }
    } catch (IOException e) {
      responseJSON = String.format("{\"status\":\"NG\", \"message\":\"%s\"}",
          e.getMessage());
    }

    System.out.println(responseJSON);
  }

}
