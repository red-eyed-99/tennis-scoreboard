package paginator;

import exceptions.PageNotFoundException;
import lombok.Getter;
import models.entities.Match;

import java.util.ArrayList;
import java.util.List;

public class MatchesPaginator {

    private static final int PAGE_ROWS_NUMBER = 5;

    private static final int MAX_VISIBLE_PAGES = 5;
    private static final int OFFSET_FROM_CURRENT_PAGE = 2;

    @Getter
    private final int totalPages;

    private final List<Match> matches;

    public MatchesPaginator(List<Match> matches) {
        this.matches = matches;

        var totalMatches = matches.size();

        if (totalMatches % PAGE_ROWS_NUMBER == 0) {
            totalPages = totalMatches / PAGE_ROWS_NUMBER;
        } else {
            totalPages = totalMatches / PAGE_ROWS_NUMBER + 1;
        }
    }

    public List<Match> getMatchesFromPage(int pageNumber) throws PageNotFoundException {
        var matches = new ArrayList<Match>();

        var offset = (pageNumber - 1) * PAGE_ROWS_NUMBER;
        var matchesNumber = PAGE_ROWS_NUMBER;

        var lastMatchIndex = this.matches.size() - 1;

        if (offset > lastMatchIndex && lastMatchIndex >= 0) {
            throw new PageNotFoundException("Page not found");
        }

        if (lastMatchIndex - offset < PAGE_ROWS_NUMBER) {
            matchesNumber = this.matches.size() - offset;
        }

        var rowIndex = offset;
        var matchesAdded = 0;

        while (matchesAdded < matchesNumber) {
            matches.add(this.matches.get(rowIndex));

            rowIndex++;
            matchesAdded++;
        }

        return matches;
    }

    public PagesRange getPagesRange(int pageNumber) {
        var startPageNumber = determineStartPageNumber(pageNumber);
        var endPageNumber = determineEndPageNumber(pageNumber);

        return new PagesRange(startPageNumber, endPageNumber);
    }

    private int determineStartPageNumber(int pageNumber) {

        if (pageNumber == 1 || pageNumber == 2) {
            return 1;
        }

        if (pageNumber == totalPages || pageNumber == totalPages - 1) {
            if (totalPages - MAX_VISIBLE_PAGES >= 0) {
                return totalPages - MAX_VISIBLE_PAGES + 1;
            }
        }

        return pageNumber - OFFSET_FROM_CURRENT_PAGE;
    }

    private int determineEndPageNumber(int pageNumber) {

        if (pageNumber == totalPages || pageNumber == totalPages - 1) {
            return totalPages;
        }

        if (pageNumber == 1 || pageNumber == 2) {
            if (totalPages >= MAX_VISIBLE_PAGES) {
                return MAX_VISIBLE_PAGES;
            }
        }

        if (totalPages <= MAX_VISIBLE_PAGES) {
            return totalPages;
        }

        return pageNumber + OFFSET_FROM_CURRENT_PAGE;
    }
}
