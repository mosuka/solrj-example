package com.github.mosuka.apache.solr.example.cmd;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.codehaus.jackson.map.ObjectMapper;

public class SearchCommand implements Command {

  @Override
  public void execute(Map<String, Object> attrs) {
    Map<String, Object> responseMap = new LinkedHashMap<String, Object>();
    String responseJSON = null;

    try {
      SolrClient solrClient = null;
      try {
        solrClient = new HttpSolrClient.Builder((String) attrs.get("solr_url")).build();

        SolrQuery query = new SolrQuery((String) attrs.get("query"));
        query.addField("*");
        query.addField("score");
        QueryResponse response = solrClient.query(query);

        List<Map<String, Object>> documentList = new LinkedList<Map<String, Object>>();
        SolrDocumentList docList = response.getResults();
        for (SolrDocument doc : docList) {
          Map<String, Object> documentMap = new HashMap<String, Object>();
          for (String fieldName : doc.getFieldNames()) {
            Object fieldValue = doc.getFieldValue(fieldName);
            documentMap.put(fieldName, fieldValue);
          }
          documentMap.put("score", doc.getFieldValue("score"));
          documentList.add(documentMap);
        }

        responseMap.put("status", response.getStatus());
        responseMap.put("message", "OK");
        responseMap.put("QTime", response.getQTime());
        responseMap.put("maxScore", response.getResults().getMaxScore());
        responseMap.put("numFound", response.getResults().getNumFound());
        responseMap.put("result", documentList);
      } catch (IOException e) {
        responseMap.put("status", -1);
        responseMap.put("message", e.getMessage());
      } catch (SolrServerException e) {
        responseMap.put("status", -1);
        responseMap.put("message", e.getMessage());
      } finally {
        if (solrClient != null) {
          solrClient.close();
        }
      }
    } catch (IOException e) {
      responseMap.put("status", 1);
      responseMap.put("message", e.getMessage());
    }

    try {
      ObjectMapper mapper = new ObjectMapper();
      responseJSON = mapper.writeValueAsString(responseMap);
    } catch (IOException e) {
      responseJSON = String.format("{\"status\":1, \"message\":\"%s\"}", e.getMessage());
    }
    System.out.println(responseJSON);
  }

}
