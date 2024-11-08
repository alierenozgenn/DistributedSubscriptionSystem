import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Capacity implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int serverStatus;
    private long timestamp;

    public Capacity(int serverStatus, long timestamp) {
        this.serverStatus = serverStatus;
        this.timestamp = timestamp;
    }

    public int getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(int serverStatus) {
        this.serverStatus = serverStatus;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(new Date(timestamp));
        
        return "Capacity { " +
                "serverStatus = " + serverStatus +
                ", timestamp = " + formattedDate +
                " }";
    }
    
}
