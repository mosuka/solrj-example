# solrj-example

## Examples for standalone Solr

### Add document to standalone Solr

```
$ java -jar solrj-example.jar add -s http://localhost:8983/solr/collection1 -d '{"id":"1","title_txt_ja":"Solr","description_txt_ja":"Solr is a full text search engine."}'
```

### Search documents from standalone Solr

```
$ java -jar solrj-example.jar search -s http://localhost:8983/solr/collection1 -q title_txt_ja:Solr
```

### Delete document to standalone Solr

```
$ java -jar solrj-example.jar delete -s http://localhost:8983/solr/collection1 -u 1
```

## Examples for SolrCloud

### Add document to SolrCloud

```
$ java -jar solrj-example.jar add -z localhost:2181 -r /solr -c collection1 -d '{"id":"1","title_txt_ja":"SolrCloud","description_txt_ja":"SolrCloud is the name of a set of new distributed capabilities in Solr."}'
```

### Search documents from SolrCloud

```
$ java -jar solrj-example.jar search -z localhost:2181 -r /solr -c collection1 -q title_txt_ja:SolrCloud
```

### Delete document to SolrCloud

```
$ java -jar solrj-example.jar delete -z localhost:2181 -r /solr -c collection1 -u 1
```
