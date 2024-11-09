import socket
import json
import matplotlib.pyplot as plt

# Sunucu ayarları
HOST = 'localhost'
PORT = 12348  # Plotter için kullanılacak port

# Sunucu ID'lerine göre renkler
server_colors = {
    "server1_status": "red",
    "server2_status": "green",
    "server3_status": "blue"
}

# Plotter'ı başlat
def start_plotter():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((HOST, PORT))
    server_socket.listen(5)
    print(f'Plotter started on {HOST}:{PORT}')

    while True:
        client_socket, addr = server_socket.accept()
        print(f'Connection from {addr}')
        
        # Veri alma
        data = client_socket.recv(1024).decode('utf-8')
        if data:
            try:
                # JSON verisini ayrıştır ve plot için kullan
                capacity_data = json.loads(data)
                plot_capacity(capacity_data)
            except json.JSONDecodeError:
                print("Received invalid JSON data")
        
        client_socket.close()

# Kapasite verilerini plot et
def plot_capacity(capacity_data):
    try:
        # Kapasite verilerini sunucu ID'lerine göre ayır
        server_ids = list(capacity_data.keys())
        server_status = [capacity_data[server_id] for server_id in server_ids]
        colors = [server_colors.get(server_id, 'gray') for server_id in server_ids]  # Renkleri atar

        # Grafik oluşturma
        plt.clf()  # Önceki grafiği temizle
        plt.bar(server_ids, server_status, color=colors)
        plt.title('Server Capacity Status')
        plt.xlabel('Server ID')
        plt.ylabel('Capacity Status')
        plt.ylim(0, 1000)  # Kapasite değerlerinin ölçeğini ayarla

        # Legend (açıklama) ekleme
        for server_id, color in server_colors.items():
            plt.bar(0, 0, color=color, label=server_id)  # Dummy bar for legend
        plt.legend(title="Sunucu Renkleri")
        
        plt.draw()  # Etkileşimli çizim
        plt.pause(0.05)  # Grafiği güncellemek için küçük bir bekleme süresi
    except KeyError as e:
        print(f"Missing data in capacity information: {e}")
    except TypeError as e:
        print(f"Invalid data type in capacity information: {e}")

if __name__ == '__main__':
    plt.ion()  # Etkileşimli mod
    start_plotter()
