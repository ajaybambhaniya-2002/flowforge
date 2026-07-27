package com.flowforge.common.dto.request;
// common request and response DTO created that use normaly in most of microservice so common DTO are created here
public class PaginationRequest {
    private int page = 0;
    private int size = 10;

    //This is often required by frameworks like Spring, Hibernate, or Jackson. They use reflection to create objects and then set values via setters. Without a no‑arg constructor, these frameworks can’t easily instantiate your class.
    public PaginationRequest() {
    }

    public PaginationRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        if (page >= 0) {
            this.page = page;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        if(size > 0){
            this.size = size;
        }

    }



}
