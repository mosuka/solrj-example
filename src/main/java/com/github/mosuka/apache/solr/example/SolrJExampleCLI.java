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
    ArgumentParser argumentParser =
        ArgumentParsers.newArgumentParser("java solr-example.jar");

    Subparsers commandSubpersers = argumentParser.addSubparsers()
        .title("Available Commands").metavar("COMMAND");

    Subparser addCmdSubParser = commandSubpersers.addParser("add")
        .help("Add data to Solr.").setDefault("command", new AddCommand());
    addCmdSubParser.addArgument("-s", "--solr-url").help("Solr URL.");
    addCmdSubParser.addArgument("-d", "--data")
        .help("Document data formatted using JSON.");

    Subparser deleteCmdSubParser =
        commandSubpersers.addParser("delete").help("Delete data from Solr.")
            .setDefault("command", new DeleteCommand());
    deleteCmdSubParser.addArgument("-s", "--solr-url")
        .help("Index directory path.");
    deleteCmdSubParser.addArgument("-d", "--data")
        .help("Document data formatted using JSON.");

    Subparser searchCmdSubParser =
        commandSubpersers.addParser("search").help("Search data of index.")
            .setDefault("command", new SearchCommand());
    searchCmdSubParser.addArgument("-s", "--solr-url")
        .help("Index directory path.");
    searchCmdSubParser.addArgument("-q", "--query")
        .help("Query to search index.");

    try {
      Namespace ns = argumentParser.parseArgs(args);
      Command command = ns.get("command");
      command.execute(ns.getAttrs());
    } catch (ArgumentParserException e) {
      argumentParser.handleError(e);
    }
  }
}
