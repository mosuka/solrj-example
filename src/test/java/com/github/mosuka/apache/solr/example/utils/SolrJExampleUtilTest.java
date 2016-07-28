package com.github.mosuka.apache.solr.example.utils;

import java.io.IOException;

import org.apache.solr.common.SolrInputDocument;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import junit.framework.TestCase;

public class SolrJExampleUtilTest extends TestCase {

  public void testCreateDocument() throws JsonParseException, JsonMappingException, IOException {
    String data =
        "{\"id\":\"1\",\"title_txt_en\":\"SolrJ\",\"description_txt_en\":\"SolrJ is an API that makes it easy for Java applications to talk to Solr.\"}";

    SolrInputDocument document = SolrJExampleUtil.createDocument(data);

    String expectedId = "1";
    String actualId = document.getFieldValue("id").toString();
    assertEquals(expectedId, actualId);

    String expectedTitle = "SolrJ";
    String actualTitle = document.getFieldValue("title_txt_en").toString();
    assertEquals(expectedTitle, actualTitle);

    String expectedDescription =
        "SolrJ is an API that makes it easy for Java applications to talk to Solr.";
    String actualDescription = document.getFieldValue("description_txt_en").toString();
    assertEquals(expectedDescription, actualDescription);
  }

}
