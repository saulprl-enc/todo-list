package com.todolist.todolistbackend.web;

import java.util.List;

public class PaginatedData<T> {
    private int currentPage;
    private int totalPages;
    private int totalItems;
    private int size;
    private List<T> data;
    private String nextPage;
    private String previousPage;

    public PaginatedData(int currentPage, int totalPages, int size, List<T> data) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.size = size;
        this.data = data;
    }

    public PaginatedData(int currentPage, int totalPages, int totalItems, int size, List<T> data) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.size = size;
        this.data = data;
    }

    public PaginatedData(int currentPage, int totalPages, List<T> data) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.data = data;
        this.size = 10;
    }

    public PaginatedData(int currentPage, int totalPages, int size, List<T> data, String nextPage, String previousPage) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.size = size;
        this.data = data;
        this.nextPage = nextPage;
        this.previousPage = previousPage;
    }

    public PaginatedData(int currentPage, int totalPages, List<T> data, String nextPage, String previousPage) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.data = data;
        this.nextPage = nextPage;
        this.previousPage = previousPage;
    }

    public PaginatedData(int currentPage, int totalPages, int totalItems, int size, List<T> data, String nextPage, String previousPage) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.size = size;
        this.data = data;
        this.nextPage = nextPage;
        this.previousPage = previousPage;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }
}
