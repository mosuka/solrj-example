# solrj-example

## Examples

### Add document

```
$ java -jar solrj-example.jar add -s "http://localhost:8983/solr/collection1" -d '{"id":"1","title":"Lucene","description":"Lucene is an OSS."}'
```

### Search documents

```
$ java -jar solrj-example.jar search -s "http://localhost:8983/solr/collection1" -q title:Lucene
```

### Update document

```
$ java -jar solrj-example.jar update -s "http://localhost:8983/solr/collection1" -d '{"id":"1","title":"Lucene","description":"Lucene is a Full-text search library."}'
```

### Delete document

```
$ java -jar solrj-example.jar delete -s "http://localhost:8983/solr/collection1" -v 1
```
