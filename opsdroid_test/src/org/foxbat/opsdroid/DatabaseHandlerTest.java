package org.foxbat.opsdroid;

import android.test.AndroidTestCase;
import org.foxbat.opsdroid.model.DatabaseHandler;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.example.opsdriod.MainActivityTest \
 * com.example.opsdriod.tests/android.test.InstrumentationTestRunner
 */
public class DatabaseHandlerTest extends AndroidTestCase {

    private DatabaseHandler databasehandler;
    private boolean isSetUpDone = true;

    @Override
    public void setUp() {
        if (isSetUpDone) {
         //   RenamingDelegatingContext context
           //         = new RenamingDelegatingContext(getContext(), "test_");
            databasehandler = new DatabaseHandler();
        }
        isSetUpDone = false;
    }


    public void testDatabaseHandler() {


    }


}
