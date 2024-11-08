class Configuration
  attr_accessor :fault_tolerance_level, :method

  # Yapılandırmayı yüklerken varsayılan değerler atayabiliriz
  def initialize(fault_tolerance_level = 1, method = "STRT")
    @fault_tolerance_level = fault_tolerance_level
    @method = method
  end

  # Konfigürasyonu bir dosyadan yükleme metodu
  def self.load_from_file(file_path)
    config_data = {}
    File.readlines(file_path).each do |line|
      key, value = line.split('=').map(&:strip)
      config_data[key] = value
    end

    # Yüklenen verilere göre Configuration nesnesi oluşturuyoruz
    fault_tolerance_level = config_data["fault_tolerance_level"].to_i
    method = config_data["method"]

    # Yeni Configuration nesnesi döndürülüyor
    new(fault_tolerance_level, method)
  end

  # Verileri kontrol ederek varsayılan değerleri kullanma veya hata fırlatma
  def validate!
    if @fault_tolerance_level <= 0
      raise "Fault tolerance level must be a positive integer."
    end

    valid_methods = ["STRT", "CPCTY"] # Geçerli method türleri
    unless valid_methods.include?(@method)
      raise "Invalid method specified. Must be one of: #{valid_methods.join(', ')}"
    end
  end

  # Yapılandırmayı metin olarak gösterme
  def to_s
    "Configuration: fault_tolerance_level = #{@fault_tolerance_level}, method = #{@method}"
  end
end

# Kullanım örneği:
# config = Configuration.load_from_file("dist_subs.conf")
# config.validate!
# puts config
