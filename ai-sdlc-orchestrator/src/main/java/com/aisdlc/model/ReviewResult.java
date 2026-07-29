import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResult {

    private boolean approved;

    private String summary;

    private List<String> issues;

    private List<String> recommendations;

}