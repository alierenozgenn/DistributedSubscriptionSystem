import java.io.Serializable;

public class Message implements Serializable {
    // Seri sürüm kimliği (sürüm uyumluluğu için)
    private static final long serialVersionUID = 1L;

    private String demand;
    private String response;

    // Ana yapıcı: Hem demand hem response alan
    public Message(String demand, String response) {
        this.demand = demand;
        this.response = response;
    }

    // Sadece demand alan yapıcı (response boş olarak ayarlanır)
    public Message(String demand) {
        this(demand, "");
    }

    // Boş yapıcı (both demand and response boş olarak ayarlanır)
    public Message() {
        this("", "");
    }

    // Getter ve setter metodları
    public String getDemand() {
        return demand;
    }

    public void setDemand(String demand) {
        this.demand = demand;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "Message { " +
               "demand='" + demand + '\'' +
               ", response='" + response + '\'' +
               " }";
    }
}
