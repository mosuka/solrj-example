package com.github.mosuka.apache.solr.example.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class SolrJExampleUtil {

  public static SolrInputDocument createDocument(String dataStr)
      throws JsonParseException, JsonMappingException, IOException {
    Map<String, Object> dataMap = new ObjectMapper().readValue(dataStr,
        new TypeReference<HashMap<String, Object>>() {
        });

    SolrInputDocument document = new SolrInputDocument();

    for (Iterator<String> i = dataMap.keySet().iterator(); i.hasNext();) {
      String fieldName = i.next();
      Object fieldValue = dataMap.get(fieldName);
      document.addField(fieldName, fieldValue);
    }

    return document;
  }

}
