package uk.co.mpkdashx.CBTT;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;

public class StartFragmentActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FragmentManager fm = getSupportFragmentManager();

		// Create the list fragment and add it as our sole content.
		if (fm.findFragmentById(android.R.id.content) == null) {
			StartFragment list = new StartFragment();
			fm.beginTransaction().add(android.R.id.content, list).commit();
		}

	}
	public static class StartFragment extends SherlockListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	}
}
