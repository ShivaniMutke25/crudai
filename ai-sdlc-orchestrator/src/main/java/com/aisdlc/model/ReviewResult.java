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