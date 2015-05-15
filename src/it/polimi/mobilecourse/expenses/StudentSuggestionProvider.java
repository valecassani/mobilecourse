package it.polimi.mobilecourse.expenses;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by valeriocassani on 14/05/15.
 */
public class StudentSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "it.polimi.mobilecourse.expenses.StudentSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public StudentSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}