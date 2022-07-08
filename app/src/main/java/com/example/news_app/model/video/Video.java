package com.example.news_app.model.video;

public class Video {
    private String kind;
    private String etag;
    private String id;
    private Snippet snippet;
    private ContentDetails contentDetails;


    public ContentDetails getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(ContentDetails contentDetails) {
        this.contentDetails = contentDetails;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    @Override
    public String toString() {
        return "kind='" + kind + '\'' +
                ", etag='" + etag + '\'' +
                ", id='" + id + '\'' +
                ", snippet=" + snippet;
    }
}
