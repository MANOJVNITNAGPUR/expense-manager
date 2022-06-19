package org.expense.manager.repo.dao;

import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TripDao {
    private long id;
    private String name;
    @NonNull
    private Set<String> categories;
    @NonNull
    private Set<String> participants;
}
