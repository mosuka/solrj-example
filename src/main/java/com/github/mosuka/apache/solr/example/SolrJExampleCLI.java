package com.github.mosuka.apache.solr.example;

import com.github.mosuka.apache.solr.example.cmd.AddCommand;
import com.github.mosuka.apache.solr.example.cmd.Command;
import com.github.mosuka.apache.solr.example.cmd.DeleteCommand;
import com.github.mosuka.apache.solr.example.cmd.SearchCommand;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;

/*
 * $ ./bin/solr start -h localhost -p 8983 -d ./server -s ./server/solr
 * $ ./bin/solr create -c collection1 -d server/solr/configsets/data_driven_schema_configs/conf -n collection1_configsets -p 8983
 */

public class SolrJExampleCLI {
  public static void main(String[] args) {
    ArgumentParser argumentParser = ArgumentParsers.newArgumentParser("java solr-example.jar");

    Subparsers commandSubpersers =
        argumentParser.addSubparsers().title("Available Commands").metavar("COMMAND");

    Subparser addCmdSubParser = commandSubpersers.addParser("add").help("Add data to Solr.")
        .setDefault("command", new AddCommand());
    addCmdSubParser.addArgument("-s", "--solr-url")
        .help("Solr URL.\nExample: http://localhost:8983/solr/collection1");
    addCmdSubParser.addArgument("-z", "--zookeeper-host")
        .help("ZooKeeper host address.\nExample: localhost:2181");
    addCmdSubParser.addArgument("-r", "--zookeeper-chroot").setDefault("/solr")
        .help("ZooKeeper chroot.\nExample: /solr");
    addCmdSubParser.addArgument("-c", "--collection").setDefault("collection1")
        .help("Index collection name. Required if you use the -z parameter.\nExample: collection1");
    addCmdSubParser.addArgument("-d", "--data")
        .help("Document data formatted using JSON.\nExample: {\"id\":\"1\",\"title\":\"SolrJ\"}");

    Subparser deleteCmdSubParser = commandSubpersers.addParser("delete")
        .help("Delete data from Solr.").setDefault("command", new DeleteCommand());
    deleteCmdSubParser.addArgument("-s", "--solr-url")
        .help("Solr URL.\nExample: http://localhost:8983/solr/collection1");
    deleteCmdSubParser.addArgument("-z", "--zookeeper-host")
        .help("ZooKeeper host address.\nExample: localhost:2181");
    deleteCmdSubParser.addArgument("-r", "--zookeeper-chroot").setDefault("/solr")
        .help("ZooKeeper chroot.\nExample: /solr");
    deleteCmdSubParser.addArgument("-c", "--collection").setDefault("collection1")
        .help("Index collection name. Required if you use the -z parameter.\nExample: collection1");
    deleteCmdSubParser.addArgument("-u", "--unique-id")
        .help("Unique ID of the data to be deleted.\nExample: 1");

    Subparser searchCmdSubParser = commandSubpersers.addParser("search")
        .help("Search data of index.").setDefault("command", new SearchCommand());
    searchCmdSubParser.addArgument("-s", "--solr-url")
        .help("Solr URL.\nExample: http://localhost:8983/solr/collection1");
    searchCmdSubParser.addArgument("-z", "--zookeeper-host")
        .help("ZooKeeper host address.\nExample: localhost:2181");
    searchCmdSubParser.addArgument("-r", "--zookeeper-chroot").setDefault("/solr")
        .help("ZooKeeper chroot.\nExample: /solr");
    searchCmdSubParser.addArgument("-c", "--collection").setDefault("collection1")
        .help("Index collection name. Required if you use the -z parameter.\nExample: collection1");
    searchCmdSubParser.addArgument("-q", "--query")
        .help("Query to search index.\nExample: title:SolrJ");

    try {
      Namespace ns = argumentParser.parseArgs(args);
      Command command = ns.get("command");
      command.execute(ns.getAttrs());
    } catch (ArgumentParserException e) {
      argumentParser.handleError(e);
      System.exit(1);
    }
  }
}
