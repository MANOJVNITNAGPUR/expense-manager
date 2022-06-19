package org.expense.manager.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripDto {
    private long id;
    private String name;
    private Set<String> categories;
    private Set<String> participants;
}
