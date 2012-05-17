package uk.co.mpkdashx.CBTT;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioGroup.LayoutParams;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 *
 * @author Martin Kemp <martin.kemp@capgemini.com>
 * @version 1.0
 * @since 10/05/2012
 */

public class MainActivity extends SherlockFragmentActivity {

	/** The m tab host. */
	TabHost mTabHost;

	/** The m view pager. */
	ViewPager mViewPager;

	/** The m tabs adapter. */
	TabsAdapter mTabsAdapter;

	/*
	 * (non-Javadoc)
	 *
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		mViewPager = (ViewPager) findViewById(R.id.pager);

		mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

		mTabsAdapter.addTab(mTabHost.newTabSpec("start").setIndicator("Start"),
				StartFragment.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec("morning").setIndicator("Morning"),
				MorningFragment.class, null);
		mTabsAdapter.addTab(mTabHost.newTabSpec("midday")
				.setIndicator("midday"), MiddayFragment.class, null);
		mTabsAdapter.addTab(
				mTabHost.newTabSpec("afternoon").setIndicator("afternoon"),
				AfternoonFragment.class, null);

		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os
	 * .Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost. It relies on a
	 * trick. Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show. This is not sufficient for switching
	 * between pages. So instead we make the content part of the tab host 0dp
	 * high (it is not shown) and the TabsAdapter supplies its own dummy view to
	 * show as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct paged in the ViewPager whenever the selected tab
	 * changes.
	 */

	public static class TabsAdapter extends FragmentPagerAdapter implements
			TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

		/** The m context. */
		private final Context mContext;

		/** The m tab host. */
		private final TabHost mTabHost;

		/** The m view pager. */
		private final ViewPager mViewPager;

		/** The m tabs. */
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		/**
		 * The Class TabInfo.
		 */
		static final class TabInfo {

			/** The tag. */
			private final String tag;

			/** The clss. */
			private final Class<?> clss;

			/** The args. */
			private final Bundle args;

			/**
			 * Instantiates a new tab info.
			 *
			 * @param _tag
			 *            the _tag
			 * @param _class
			 *            the _class
			 * @param _args
			 *            the _args
			 */
			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		/**
		 * A factory for creating DummyTab objects.
		 */
		static class DummyTabFactory implements TabHost.TabContentFactory {

			/** The m context. */
			private final Context mContext;

			/**
			 * Instantiates a new dummy tab factory.
			 *
			 * @param context
			 *            the context
			 */
			public DummyTabFactory(Context context) {
				mContext = context;
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * android.widget.TabHost.TabContentFactory#createTabContent(java
			 * .lang.String)
			 */
			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		/**
		 * Instantiates a new tabs adapter.
		 *
		 * @param activity
		 *            the activity
		 * @param tabHost
		 *            the tab host
		 * @param pager
		 *            the pager
		 */
		public TabsAdapter(FragmentActivity activity, TabHost tabHost,
				ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mTabHost = tabHost;
			mViewPager = pager;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		/**
		 * Adds the tab.
		 *
		 * @param tabSpec
		 *            the tab spec
		 * @param clss
		 *            the clss
		 * @param args
		 *            the args
		 */
		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see android.support.v4.view.PagerAdapter#getCount()
		 */
		@Override
		public int getCount() {
			return mTabs.size();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
		 */
		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang
		 * .String)
		 */
		@Override
		public void onTabChanged(String tabId) {
			int position = mTabHost.getCurrentTab();
			mViewPager.setCurrentItem(position);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled
		 * (int, float, int)
		 */
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
		 * (int)
		 */
		@Override
		public void onPageSelected(int position) {
			// Unfortunately when TabHost changes the current tab, it kindly
			// also takes care of putting focus on it when not in touch mode.
			// The jerk.
			// This hack tries to prevent this from pulling focus out of our
			// ViewPager.
			TabWidget widget = mTabHost.getTabWidget();
			int oldFocusability = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			mTabHost.setCurrentTab(position);
			widget.setDescendantFocusability(oldFocusability);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#
		 * onPageScrollStateChanged(int)
		 */
		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}

	/**
	 * The Class StartFragment.
	 */
	public static class StartFragment extends SherlockFragment {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater
		 * , android.view.ViewGroup, android.os.Bundle)
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			TextView textView = new TextView(getActivity());
			textView.setTextSize(16);
			textView.setGravity(Gravity.CENTER_VERTICAL);
			textView.setText(R.string.startText);
			return textView;

		}
	}

	/**
	 * The Class MorningFragment.
	 */
	public static class MorningFragment extends SherlockFragment {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater
		 * , android.view.ViewGroup, android.os.Bundle)
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final ListView morninglistView = new ListView(getActivity());
			String[] morning = getResources().getStringArray(
					R.array.morning_array);

			morninglistView.setAdapter(new ArrayAdapter<String>(getActivity(),
					R.layout.list_item, morning));
			morninglistView.setTextFilterEnabled(true);
			morninglistView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> argo0, View arg1,
						int pos, long id) {
					String data = null;
					if (pos == 0 || pos == 5 || pos == 9 || pos == 13) {

						data = morninglistView.getItemAtPosition(pos)
								.toString();
						if (pos == 0) {
							Intent intent = new Intent(
									android.content.Intent.ACTION_VIEW,
									Uri.parse("geo:0,0?q=Birmingham,+New+Street+Station+(Stop+VA)"));
							Log.v("clicked", data + " @ " + "pos " + pos
									+ " Launched maps with query: " + intent);
							startActivity(intent);
						} else if (pos == 5) {
							Intent intent = new Intent(
									android.content.Intent.ACTION_VIEW,
									Uri.parse("geo:0,0?q=Radisson+Blu+Hotel+Birmingham"));
							Log.v("clicked", data + " @ " + "pos " + pos
									+ " Launched maps with query: " + intent);
							startActivity(intent);
						} else if (pos == 9) {
							Intent intent = new Intent(
									android.content.Intent.ACTION_VIEW,
									Uri.parse("geo:0,0?q=Crowne+Plaza+Hotel+Birmingham+City+Centre"));
							Log.v("clicked", data + " @ " + "pos " + pos
									+ " Launched maps with query: " + intent);
							startActivity(intent);
						} else if (pos == 13) {
							Intent intent = new Intent(
									android.content.Intent.ACTION_VIEW,
									Uri.parse("geo:0,0?q=Novotel+Birmingham+Centre"));
							Log.v("clicked", data + " @ " + "pos " + pos
									+ " Launched maps with query: " + intent);
							startActivity(intent);
						}

					} else {
						data = morninglistView.getItemAtPosition(pos)
								.toString();
						String[] split = data.split(":");
						int H = Integer.parseInt(split[0]);
						int M = Integer.parseInt(split[1]);
						Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
						i.putExtra(AlarmClock.EXTRA_HOUR, H);
						i.putExtra(AlarmClock.EXTRA_MINUTES, M);
						startActivity(i);
						Log.v("clicked", data + " @ " + "pos " + pos
								+ " set alarm for: " + data);
					}

				}
			});
			return morninglistView;
		}
	}

	/**
	 * The Class MiddayFragment.
	 */
	public static class MiddayFragment extends SherlockFragment {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater
		 * , android.view.ViewGroup, android.os.Bundle)
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final ListView middaylistView = new ListView(getActivity());
			String[] midday = getResources().getStringArray(
					R.array.midday_array);

			middaylistView.setAdapter(new ArrayAdapter<String>(getActivity(),
					R.layout.list_item, midday));
			middaylistView.setTextFilterEnabled(true);
			middaylistView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> argo0, View arg1,
						int pos, long id) {
					String data = null;
					if (pos == 0 || pos == 5) {

						data = middaylistView.getItemAtPosition(pos).toString();
						if (pos == 0) {
							Intent intent = new Intent(
									android.content.Intent.ACTION_VIEW,
									Uri.parse("geo:0,0?q=1+Avenue+Road,+Aston,+Birmingham"));
							Log.v("clicked", data + " @ " + "pos " + pos
									+ " Launched maps with query: " + intent);
							startActivity(intent);
						} else if (pos == 5) {
							Intent intent = new Intent(
									android.content.Intent.ACTION_VIEW,
									Uri.parse("geo:0,0?q=Birmingham,+Upper+Bull+St+(Stop+BA),+Birmingham,+West+Midlands+B4,+United+Kingdom&hl=en&ll=52.482048,-1.896493&spn=0.011069,0.01929&geocode=FQLQIAMdzA_j_w&hnear=Birmingham,+Upper+Bull+St+(Stop+BA)&t=m&z=16"));
							Log.v("clicked", data + " @ " + "pos " + pos
									+ " Launched maps with query: " + intent);
							startActivity(intent);
						}

					} else {
						data = middaylistView.getItemAtPosition(pos).toString();
						String[] split = data.split(":");
						int H = Integer.parseInt(split[0]);
						int M = Integer.parseInt(split[1]);
						Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
						i.putExtra(AlarmClock.EXTRA_HOUR, H);
						i.putExtra(AlarmClock.EXTRA_MINUTES, M);
						startActivity(i);
						Log.v("clicked", data + " @ " + "pos " + pos
								+ " set alarm for: " + data);
					}

				}
			});
			return middaylistView;
		}
	}

	/**
	 * The Class AfternoonFragment.
	 */
	public static class AfternoonFragment extends SherlockFragment {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater
		 * , android.view.ViewGroup, android.os.Bundle)
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final ListView afternoonlistView = new ListView(getActivity());
			String[] afternoon = getResources().getStringArray(
					R.array.afternoon_array);

			afternoonlistView.setAdapter(new ArrayAdapter<String>(
					getActivity(), R.layout.list_item, afternoon));
			afternoonlistView.setTextFilterEnabled(true);
			afternoonlistView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> argo0, View arg1,
						int pos, long id) {
					String data = null;
					if (pos == 0 || pos == 5 || pos == 9 || pos == 13) {
						data = afternoonlistView.getItemAtPosition(pos)
								.toString();
						if (pos == 0) {
							Intent intent = new Intent(
									android.content.Intent.ACTION_VIEW, Uri
											.parse("geo:0,0?q=Capgemini+Aston"));
							Log.v("clicked", data + " @ " + "pos " + pos
									+ " Launched maps with query: " + intent);
							startActivity(intent);
						} else if (pos == 5) {
							Intent intent = new Intent(
									android.content.Intent.ACTION_VIEW,
									Uri.parse("geo:0,0?q=Birmingham,+Corporation+St+(Stop+CL)"));
							Log.v("clicked", data + " @ " + "pos " + pos
									+ " Launched maps with query: " + intent);
							startActivity(intent);
						} else if (pos == 9) {
							Intent intent = new Intent(
									android.content.Intent.ACTION_VIEW,
									Uri.parse("geo:0,0?q=Radisson+Blu+Hotel+Birmingham"));
							Log.v("clicked", data + " @ " + "pos " + pos
									+ " Launched maps with query: " + intent);
							startActivity(intent);
						} else if (pos == 13) {
							Intent intent = new Intent(
									android.content.Intent.ACTION_VIEW,
									Uri.parse("geo:0,0?q=Crowne+Plaza+Hotel+Birmingham+City+Centre"));
							Log.v("clicked", data + " @ " + "pos " + pos
									+ " Launched maps with query: " + intent);
							startActivity(intent);
						} else if (pos == 17) {
							Intent intent = new Intent(
									android.content.Intent.ACTION_VIEW,
									Uri.parse("geo:0,0?q=Novotel+Birmingham+Centre"));
							Log.v("clicked", data + " @ " + "pos " + pos
									+ " Launched maps with query: " + intent);
							startActivity(intent);
						}

					} else {
						data = afternoonlistView.getItemAtPosition(pos)
								.toString();
						String[] split = data.split(":");
						int H = Integer.parseInt(split[0]);
						int M = Integer.parseInt(split[1]);
						Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
						i.putExtra(AlarmClock.EXTRA_HOUR, H);
						i.putExtra(AlarmClock.EXTRA_MINUTES, M);
						startActivity(i);
						Log.v("clicked", data + " @ " + "pos " + pos
								+ " set alarm for: " + data);
					}

				}
			});
			return afternoonlistView;
		}
	}
}
