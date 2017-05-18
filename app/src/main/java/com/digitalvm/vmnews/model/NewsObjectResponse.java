package com.digitalvm.vmnews.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by TL on 5/16/17.
 */

public class NewsObjectResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("sortBy")
    @Expose
    private String sortBy;
    @SerializedName("articles")
    @Expose
    private List<NewsObject> articles = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public List<NewsObject> getArticles() {
        return articles;
    }

    public void setArticles(List<NewsObject> articles) {
        this.articles = articles;
    }
}
