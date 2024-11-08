import java.util.List;

public class Subscriber {
    private int id;
    private String nameSurname;
    private long startDate;
    private long lastAccessed;
    private List<String> interests;
    private boolean isOnline;

    public Subscriber(int id, String nameSurname, long startDate, long lastAccessed, List<String> interests, boolean isOnline) {
        this.id = id;
        this.nameSurname = nameSurname;
        this.startDate = startDate;
        this.lastAccessed = lastAccessed;
        this.interests = interests;
        this.isOnline = isOnline;
    }

    // Getters ve Setters (gerektiğinde tüm alanlar için)
    public int getId() { return id; }
    public String getNameSurname() { return nameSurname; }
    public long getStartDate() { return startDate; }
    public long getLastAccessed() { return lastAccessed; }
    public List<String> getInterests() { return interests; }
    public boolean isOnline() { return isOnline; }
    
    public void setIsOnline(boolean online) { this.isOnline = online; }
}
