package com.todolist.todolistbackend.dto;

public class TodoStatsImpl<T> implements TodoStats<T> {
    private T globalAverage;
    private T lowAverage;
    private T mediumAverage;
    private T highAverage;

    public TodoStatsImpl() {
    }

    public TodoStatsImpl(T globalAverage, T lowAverage, T mediumAverage, T highAverage) {
        this.globalAverage = globalAverage;
        this.lowAverage = lowAverage;
        this.mediumAverage = mediumAverage;
        this.highAverage = highAverage;
    }

    @Override
    public T getGlobalAverage() {
        return globalAverage;
    }

    @Override
    public T getLowAverage() {
        return lowAverage;
    }

    @Override
    public T getMediumAverage() {
        return mediumAverage;
    }

    @Override
    public T getHighAverage() {
        return highAverage;
    }

    @Override
    public void setGlobalAverage(T globalAverage) {
        this.globalAverage = globalAverage;
    }

    @Override
    public void setLowAverage(T lowAverage) {
        this.lowAverage = lowAverage;
    }

    @Override
    public void setMediumAverage(T mediumAverage) {
        this.mediumAverage = mediumAverage;
    }

    @Override
    public void setHighAverage(T highAverage) {
        this.highAverage = highAverage;
    }
}
