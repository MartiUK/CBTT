package uk.co.mpkdashx.CBTT;

import uk.co.mpkdashx.CBTT.MainActivity.TimePickerFragment;

import com.actionbarsherlock.app.SherlockFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MorningFragment extends SherlockFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final ListView morninglistView = new ListView(getActivity());
		String[] morning = getResources().getStringArray(R.array.morning_array);

		morninglistView.setAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.list_item, morning));
		morninglistView.setTextFilterEnabled(true);
		morninglistView
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int pos, long id) {
						String data = null;
						if (pos == 0 || pos == 5 || pos == 9 || pos == 13) {

							data = morninglistView.getItemAtPosition(pos)
									.toString();
							if (pos == 0) {
								Intent intent = new Intent(
										android.content.Intent.ACTION_VIEW,
										Uri.parse("geo:0,0?q=Birmingham,+New+Street+Station+(Stop+VA)"));
								Log.v("clicked", data + " @ " + "pos " + pos
										+ " Launched maps with query: "
										+ intent);
								startActivity(intent);
							} else if (pos == 5) {
								Intent intent = new Intent(
										android.content.Intent.ACTION_VIEW,
										Uri.parse("geo:0,0?q=Radisson+Blu+Hotel+Birmingham"));
								Log.v("clicked", data + " @ " + "pos " + pos
										+ " Launched maps with query: "
										+ intent);
								startActivity(intent);
							} else if (pos == 9) {
								Intent intent = new Intent(
										android.content.Intent.ACTION_VIEW,
										Uri.parse("geo:0,0?q=Crowne+Plaza+Hotel+Birmingham+City+Centre"));
								Log.v("clicked", data + " @ " + "pos " + pos
										+ " Launched maps with query: "
										+ intent);
								startActivity(intent);
							} else if (pos == 13) {
								Intent intent = new Intent(
										android.content.Intent.ACTION_VIEW,
										Uri.parse("geo:0,0?q=Novotel+Birmingham+Centre"));
								Log.v("clicked", data + " @ " + "pos " + pos
										+ " Launched maps with query: "
										+ intent);
								startActivity(intent);
							}

						} else {
							data = morninglistView.getItemAtPosition(pos)
									.toString();
							String[] split = data.split(":");
							split[0] = split[0].replace("*", "");
							split[1] = split[1].replace("*", "");
							int H = Integer.parseInt(split[0]);
							int M = Integer.parseInt(split[1]);
							

							new MainActivity.TimePickerFragment();
							TimePickerFragment.newInstance(H, M).show(
									getActivity().getSupportFragmentManager(),
									"timePicker");

							Log.v("clicked", data + " @ " + "pos " + pos
									+ " went to TimePicker with data: " + data);
						}
						return false;

					}
				});
		return morninglistView;

	}
}
