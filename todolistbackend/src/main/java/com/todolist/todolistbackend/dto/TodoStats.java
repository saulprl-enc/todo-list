package com.todolist.todolistbackend.dto;

public interface TodoStats<T> {
    T getGlobalAverage();

    void setGlobalAverage(T globalAverage);

    T getLowAverage();

    void setLowAverage(T lowAverage);

    T getMediumAverage();

    void setMediumAverage(T mediumAverage);

    T getHighAverage();

    void setHighAverage(T highAverage);
}
