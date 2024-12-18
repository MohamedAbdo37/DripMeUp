//package edu.alexu.cse.dripmeup.service.builder;
//
//import edu.alexu.cse.dripmeup.entity.EntityIF;
//import edu.alexu.cse.dripmeup.entity.product.ItemEntity;
//
//public class ProductItemBuilder implements ProductBuilderIF {
//
//    private final ItemEntity item;
//
//    public ProductItemBuilder() {
//        this.item = new ItemEntity();
//    }
//
//    public void buildPrice(double price) {
//        this.item.setPrice(price);
//    }
//
//    public void buildProductName(String productName) {
//        this.item.setProductName(productName);
//    }
//
//
//    @Override
//    public void build() {
//        // TODO
//    }
//
//    @Override
//    public EntityIF getResult() {
//        return this.item;
//    }
//}
