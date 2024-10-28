package paginator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PagesRange {

    private final int startPageNumber;
    private final int endPageNumber;
}
