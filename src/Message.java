import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String demand;
    private String response;

    // Ana yapıcı
    public Message(String demand, String response) {
        this.demand = demand;
        this.response = response;
    }

    // Sadece "demand" için bir yapıcı
    public Message(String demand) {
        this.demand = demand;
        this.response = "";
    }

    // Boş bir yapıcı
    public Message() {
        this.demand = "";
        this.response = "";
    }

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
        return "Message { demand: " + demand + ", response: " + response + " }";
    }
    
    
}
