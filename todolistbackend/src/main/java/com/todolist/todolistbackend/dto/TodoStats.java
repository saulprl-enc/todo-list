package com.todolist.todolistbackend.dto;

public class TodoStats {
    private String globalAverage;
    private String lowAverage;
    private String mediumAverage;
    private String highAverage;

    public TodoStats() {
    }

    public TodoStats(String averageCompletionInMillis, String averageLowCompletionInMillis, String averageMediumCompletionInMillis, String averageHighCompletionInMillis) {
        this.globalAverage = averageCompletionInMillis;
        this.lowAverage = averageLowCompletionInMillis;
        this.mediumAverage = averageMediumCompletionInMillis;
        this.highAverage = averageHighCompletionInMillis;
    }

    public String getGlobalAverage() {
        return globalAverage;
    }

    public void setGlobalAverage(String globalAverage) {
        this.globalAverage = globalAverage;
    }

    public String getLowAverage() {
        return lowAverage;
    }

    public void setLowAverage(String lowAverage) {
        this.lowAverage = lowAverage;
    }

    public String getMediumAverage() {
        return mediumAverage;
    }

    public void setMediumAverage(String mediumAverage) {
        this.mediumAverage = mediumAverage;
    }

    public String getHighAverage() {
        return highAverage;
    }

    public void setHighAverage(String highAverage) {
        this.highAverage = highAverage;
    }
}
