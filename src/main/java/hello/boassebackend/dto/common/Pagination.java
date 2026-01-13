package hello.boassebackend.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {
    private int currentPage;
    private int totalPages;
    private long totalCount;
    private int limit;
    private boolean hasNext;
    private boolean hasPrev;
}
