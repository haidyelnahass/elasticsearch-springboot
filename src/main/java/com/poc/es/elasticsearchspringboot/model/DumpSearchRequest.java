package com.poc.es.elasticsearchspringboot.model;

public class DumpSearchRequest {

    private Dump mustQuery;
    private Dump shouldQuery;
    private SortItems[] sortQuery;
    private Dump matchQuery;
    private int Size;
    public Dump getMustQuery() {
        return mustQuery;
    }
    public void setMustQuery(Dump mustQuery) {
        this.mustQuery = mustQuery;
    }
    public Dump getShouldQuery() {
        return shouldQuery;
    }
    public void setShouldQuery(Dump shouldQuery) {
        this.shouldQuery = shouldQuery;
    }
    public SortItems[] getSortQuery() {
        return sortQuery;
    }
    public void setSortQuery(SortItems[] sortQuery) {
        this.sortQuery = sortQuery;
    }
    public Dump getMatchQuery() {
        return matchQuery;
    }
    public void setMatchQuery(Dump matchQuery) {
        this.matchQuery = matchQuery;
    }
    public int getSize() {
        return Size;
    }
    public void setSize(int size) {
        Size = size;
    }



    
}
