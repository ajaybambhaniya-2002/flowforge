package com.flowforge.common.dto.request;

public class SortRequest {
    private String sortBy = "id";
    private String direction = "ASC";

    public SortRequest() {
    }

    public SortRequest(String sortBy, String direction) {
        this.sortBy = sortBy;
        this.direction = direction;
    }
    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }


}
