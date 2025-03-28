package nl.han.oose.dea.rest.services.dto.track;

public class TrackDTO {
    private int id;
    private String title;
    private String performer;
    private int duration;
    private String album;
    private Integer playcount;
    private String publicationDate;
    private String description;
    private boolean offlineAvailable;

    public TrackDTO() {
    }

    public TrackDTO(int id, String title, String performer, int duration, String album,
                    Integer playcount, String publicationDate, String description, boolean offlineAvailable) {
        this.id = id;
        this.title = title;
        this.performer = performer;
        this.duration = duration;
        this.album = album;
        this.playcount = playcount;
        this.publicationDate = publicationDate;
        this.description = description;
        this.offlineAvailable = offlineAvailable;
    }

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setPerformer(String performer) { this.performer = performer; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setAlbum(String album) { this.album = album; }
    public void setPlaycount(Integer playcount) { this.playcount = playcount; }
    public void setPublicationDate(String publicationDate) { this.publicationDate = publicationDate; }
    public void setDescription(String description) { this.description = description; }
    public void setOfflineAvailable(boolean offlineAvailable) { this.offlineAvailable = offlineAvailable; }


    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getPerformer() { return performer; }
    public int getDuration() { return duration; }
    public String getAlbum() { return album; }
    public Integer getPlaycount() { return playcount; }
    public String getPublicationDate() { return publicationDate; }
    public String getDescription() { return description; }
    public boolean isOfflineAvailable() { return offlineAvailable; }
}

