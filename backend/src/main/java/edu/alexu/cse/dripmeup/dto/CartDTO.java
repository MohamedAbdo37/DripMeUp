package edu.alexu.cse.dripmeup.dto;

public class CartDTO {
    private CartProductDTO element;
    private int amount ;

    public CartDTO(CartProductDTO element, int amount) {
        this.element = element;
        this.amount = amount;
    }

    public CartProductDTO getElement() {
        return this.element;
    }

    public void setElement(CartProductDTO element) {
        this.element = element;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
