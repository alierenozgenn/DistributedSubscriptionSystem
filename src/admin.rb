require 'socket'
require 'json'

# Message sınıfı tanımlanıyor
class Message
  attr_accessor :demand, :response

  def initialize(demand, response)
    @demand = demand
    @response = response
  end

  def to_json(*args)
    {
      demand: @demand,
      response: @response
    }.to_json(*args)
  end
end

# Capacity sınıfı tanımlanıyor
class Capacity
  attr_accessor :server_status, :timestamp

  def initialize(server_status, timestamp)
    @server_status = server_status
    @timestamp = timestamp
  end

  def to_json(*args)
    {
      server_status: @server_status,
      timestamp: @timestamp
    }.to_json(*args)
  end
end

# dist_subs.conf dosyasından ayarları okuma
def load_configuration
  config = {}
  File.readlines("dist_subs.conf").each do |line|
    key, value = line.split('=').map(&:strip)
    config[key] = value
  end
  config
end

# Sunucuların adresleri ve portları
servers = [
  { host: 'localhost', port: 12345 }, # Server1
  { host: 'localhost', port: 12346 }, # Server2
  { host: 'localhost', port: 12347 }  # Server3
]

# dist_subs.conf dosyasından ayarları yüklüyoruz
config = load_configuration
fault_tolerance_level = config["fault_tolerance_level"].to_i
method = config["method"]

# YEP yanıtı dönen sunucular için bir dizi
active_servers = []

# Sunucuları başlatma komutunu gönderme
servers.each do |server|
  begin
    socket = TCPSocket.new(server[:host], server[:port])
    message = Message.new(method, nil)
    socket.puts(message.to_json)

    response = socket.gets
    if response
      parsed_response = JSON.parse(response)
      if parsed_response['response'] == "YEP"
        puts "#{server[:host]}:#{server[:port]} responded with YEP"
        active_servers << server # YEP dönen sunucuları kaydediyoruz
      else
        puts "#{server[:host]}:#{server[:port]} responded with NOP"
      end
    else
      puts "No response received from #{server[:host]}:#{server[:port]}"
    end
  rescue => e
    puts "Error connecting to #{server[:host]}:#{server[:port]} - #{e.message}"
  ensure
    socket.close if socket
  end
end

# Aktif sunucu sayısını fault_tolerance_level ile karşılaştır
if active_servers.size < fault_tolerance_level
  puts "Warning: Active servers (#{active_servers.size}) are below the required fault tolerance level (#{fault_tolerance_level})."
end

# 5 saniyede bir kapasite sorgulama işlemi
loop do
  active_servers.each do |server|
    begin
      socket = TCPSocket.new(server[:host], server[:port])
      capacity_request = Message.new("CPCTY", nil)
      socket.puts(capacity_request.to_json)

      capacity_response = socket.gets
      if capacity_response
        parsed_capacity = JSON.parse(capacity_response)
        if parsed_capacity['server_status'] && parsed_capacity['timestamp']
          capacity = Capacity.new(parsed_capacity['server_status'], parsed_capacity['timestamp'])
          puts "#{server[:host]}:#{server[:port]} capacity status: #{capacity.server_status}, timestamp: #{capacity.timestamp}"
        else
          puts "Invalid capacity response received from #{server[:host]}:#{server[:port]}"
        end
      else
        puts "No capacity response received from #{server[:host]}:#{server[:port]}"
      end
    rescue => e
      puts "Error connecting to #{server[:host]}:#{server[:port]} - #{e.message}"
      active_servers.delete(server) # Hata oluşursa sunucuyu aktif listesinden çıkar
      puts "#{server[:host]}:#{server[:port]} has been removed from active servers due to connection issues."
    ensure
      socket.close if socket
    end
  end

  # Aktif sunucu sayısını fault_tolerance_level ile karşılaştır
  if active_servers.size < fault_tolerance_level
    puts "Fault tolerance level warning: Active servers are below required level."
  end

  sleep(5) # 5 saniye bekle
end

# Sunucunun çalışıp çalışmadığını kontrol et
def server_alive?(server)
  begin
    socket = TCPSocket.new(server[:host], server[:port])
    socket.close
    true
  rescue
    false
  end
end
