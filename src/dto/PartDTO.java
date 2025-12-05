package dto;

public class PartDTO {
	private int partId;
	private String name;
	private int stock;
	private String imageName;
	private String manufacturer;
	private String supplier;
	private int unitPrice;
	private int safetyStock;
	private String description;

	public PartDTO(int partId, String name, int stock, String imageName, String manufacturer, String supplier,
			int unitPrice, int safetyStock, String description) {
		super();
		this.partId = partId;
		this.name = name;
		this.stock = stock;
		this.imageName = imageName;
		this.manufacturer = manufacturer;
		this.supplier = supplier;
		this.unitPrice = unitPrice;
		this.safetyStock = safetyStock;
		this.description = description;
	}

	public int getPartId() {
		return partId;
	}

	public void setPartId(int partId) {
		this.partId = partId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public int getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(int unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getSafetyStock() {
		return safetyStock;
	}

	public void setSafetyStock(int safetyStock) {
		this.safetyStock = safetyStock;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
