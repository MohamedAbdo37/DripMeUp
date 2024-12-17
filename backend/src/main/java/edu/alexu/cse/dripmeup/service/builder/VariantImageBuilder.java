package edu.alexu.cse.dripmeup.service.builder;

import edu.alexu.cse.dripmeup.entity.product.VariantImageEntity;
import edu.alexu.cse.dripmeup.repository.ImageRepository;

public class VariantImageBuilder implements ProductBuilderIF{

    private final VariantImageEntity variantImage;


    public VariantImageBuilder(ImageRepository imageRepository, Long imageID) {
        this.variantImage = imageRepository.findByImageID(imageID);
    }

    public VariantImageBuilder(){
        this.variantImage = new VariantImageEntity();
    }

    @Override
    public void build() {
        // TODO Auto-generated method stub
    }

    @Override
    public VariantImageEntity getResult() {
        return this.variantImage;
    }
    
}
