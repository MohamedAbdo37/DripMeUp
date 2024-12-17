package edu.alexu.cse.dripmeup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageMetaDTO {
    public int pageNumber;
    public int pageSize;
    public int totalPages;
}
