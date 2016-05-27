package com.cyngn.munchmod;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import android.text.format.Time;

import java.util.Calendar;

/**
 * Provides Customizations for
 * the Munch app based on time of day,
 * user prefs
 */
public class MunchCustomizer {

    private static final boolean DEBUG = true;
    private static final String TAG = "MunchCustomizer";

    private MunchCustomizer() {}

    /**
     * crude time of day and day of week customizer
     */
    private enum TimeDayMode {
        MORNING(R.string.greeting_text_morning, R.string.search_term_morning),
        LUNCH(R.string.greeting_text_lunch, R.string.search_term_lunch),
        AFTERNOON(R.string.greeting_text_afternoon, R.string.search_term_afternoon),
        EVENING(R.string.greeting_text_evening, R.string.search_term_evening),
        NIGHT(R.string.greeting_text_night, R.string.search_term_night),
        LATE_NIGHT(R.string.greeting_text_late_night, R.string.search_term_late_night),
        WEEKEND_MORNING(R.string.greeting_text_weekend_morning, R.string.search_term_weekend_morning),
        WEEKEND_LUNCH(R.string.greeting_text_weekend_lunch, R.string.search_term_weekend_lunch),
        WEEKEND_AFTERNOON(R.string.greeting_text_afternoon, R.string.search_term_weekend_afternoon),
        WEEKEND_EVENING(R.string.greeting_text_weekend_evening, R.string.search_term_weekend_evening),
        WEEKEND_NIGHT(R.string.greeting_text_weekend_night, R.string.search_term_weekend_night),
        WEEKEND_LATE_NIGHT(R.string.greeting_text_weekend_late_night, R.string.search_term_weekend_late_night),
        DEFAULT(R.string.greeting_text_default, R.string.search_term_default); // no good fits

        public final int greetingTextId;
        public final int searchTermsId;
        TimeDayMode(int greetingTextId, int searchTermsId) {
            this.greetingTextId = greetingTextId;
            this.searchTermsId = searchTermsId;
        }
        public static TimeDayMode getCurrentMode() {
           return getMode(Calendar.getInstance());
        }

        public static TimeDayMode getMode(Calendar currentTime) {
            final int dayOfWeek = currentTime.get(Calendar.DAY_OF_WEEK);
            boolean isWeekday = dayOfWeek >= Calendar.MONDAY && dayOfWeek <= Calendar.FRIDAY;
            if(DEBUG) {
                Log.d(TAG, "getMode dayOfWeek=" + dayOfWeek);
            }

            final int hourOfDay = currentTime.get(Calendar.HOUR_OF_DAY);

            // Morning hours
            if (hourOfDay >= 6 && hourOfDay < 11) {
                return isWeekday? MORNING : WEEKEND_MORNING;
            }
            // Lunch hours
            if (hourOfDay >= 11 && hourOfDay < 14) {
                return isWeekday? LUNCH : WEEKEND_LUNCH;
            }
            // Afternoon hours
            if (hourOfDay >= 14 && hourOfDay < 17) {
                return isWeekday? AFTERNOON : WEEKEND_AFTERNOON;
            }
            // Evening hours
            if (hourOfDay >= 17 && hourOfDay < 21) {
                return isWeekday? EVENING : WEEKEND_EVENING;
            }
            // Night hours
            if (hourOfDay >= 17 && hourOfDay < 23) {
                return isWeekday? NIGHT : WEEKEND_NIGHT;
            }
            // Late night hours
            if (hourOfDay >= 23 || hourOfDay < 6) {
                return isWeekday? LATE_NIGHT : WEEKEND_LATE_NIGHT;
            }

            return DEFAULT;
        }
    }


    /**
     * Set the splash intro text
     * @param tv
     */
    public static void setSplashText(TextView tv) {
        tv.setText(TimeDayMode.getCurrentMode().greetingTextId);
    }


    /**
     * Get a customized comma delimited list of
     * Yelp search terms based on day of time and user prefs
     * @return
     */
    public static String getSearchTerms(Context context) {
        final String terms[] = new String[] {
                context.getString(TimeDayMode.getCurrentMode().searchTermsId),
                getUserPrefsTerms(context),
        };
        return termsToString(terms);
    }

    private static String getUserPrefsTerms(Context context) {
        return null;
    }

    /**
     * Build a comma delimited set of terms from a list
     * @param terms
     * @return
     */
    private static String termsToString(String[] terms) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (String term : terms) {
            if (term == null || term.isEmpty()) {
                continue;
            }
            if (!isFirst) {
                builder.append(",");
            } else {
                isFirst = false;
            }
            builder.append(term);
        }
        final String retVal = builder.toString();
        if (DEBUG) {
            Log.d(TAG, "termsToString: " + retVal);
        }
        return retVal;
    }

}
