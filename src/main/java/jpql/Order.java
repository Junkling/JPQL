package jpql;

import javax.persistence.*;

@Entity
@Table(name = "ORDERS")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    private int price;

    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
