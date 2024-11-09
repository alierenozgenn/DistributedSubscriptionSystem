import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Capacity implements Serializable {
    // Seri sürüm kimliği (sürüm uyumluluğu için)
    private static final long serialVersionUID = 1L;

    private int serverStatus;
    private long timestamp;

    // Tarih formatlayıcı (her çağrıda yeniden oluşturmak yerine, bir kez tanımlanır)
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Ana yapıcı
    public Capacity(int serverStatus, long timestamp) {
        this.serverStatus = serverStatus;
        setTimestamp(timestamp);
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
        if (timestamp >= 0) {
            this.timestamp = timestamp;
        } else {
            throw new IllegalArgumentException("Timestamp cannot be negative.");
        }
    }

    @Override
    public String toString() {
        String formattedDate = DATE_FORMAT.format(new Date(this.timestamp));
        return "Capacity { " +
                "serverStatus = " + serverStatus +
                ", timestamp = " + formattedDate +
                " }";
    }
}
