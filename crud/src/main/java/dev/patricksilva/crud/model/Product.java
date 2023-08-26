package dev.patricksilva.crud.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {
    private Integer id;
    private String uuid;
    private String name;
    private Integer quantity;
    private Double price;
    private String observation;
    private String Currency;
    
    
    public Product() {   	
    }
    
    public Product(Integer id, String uuid, String name, Integer quantity, Double price, String observation,
			String currency) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
		this.observation = observation;
		Currency = currency;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    // #region Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
    
    public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", uuid=" + uuid + ", name=" + name + ", quantity=" + quantity + ", price=" + price
				+ ", observation=" + observation + ", Currency=" + Currency + "]";
	}



    // #endregion
}
