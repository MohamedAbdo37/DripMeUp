package edu.alexu.cse.dripmeup.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "IMAGE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue
    private Long id;

    private String path;

    private String cloudId;

    public Image(String path, String cloudId){
        this.path = path;
        this.cloudId = cloudId;
    }
}
