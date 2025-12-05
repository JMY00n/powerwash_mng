package dto;

public class ProductDTO {
	private int productId;
	private String name;
	private String type;
	private int stock;
	private String imageName;
	
	public ProductDTO() {
		
	}
	
	public ProductDTO(int productId, String name, String type, int stock, String imageName) {
		this.productId = productId;
		this.name = name;
		this.type = type;
		this.stock = stock;
		this.imageName = imageName;
	}
	
	public int getProductId() {
		return productId;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public int getStock() {
		return stock;
	}
	
	public String getImageName() {
		return imageName;
	}
	
}
