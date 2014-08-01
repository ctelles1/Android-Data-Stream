package gyro.accel.slide;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Button;
public class MainSlideActivity extends FragmentActivity implements
		ActionBar.TabListener {

	// Defines variables and sensors

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the three primary sections of the app. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the
	 * app, one at a time.
	 */
	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// overridePendingTransition(R.anim.slide_in_left,
		// R.anim.slide_out_right);

		// Intent slideactivity = new Intent(MainActivity.this,
		// ActivityB.class);
		// startActivity(slideactivity);

		// next = (Button) findViewById(R.id.activity_button);
		// next.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View view) {
		// Toast.makeText(getApplicationContext(), "text",
		// Toast.LENGTH_SHORT).show();
		// Intent myIntent = new Intent(view.getContext(),
		// DrawerActivity.class);
		// startActivityForResult(myIntent, 0);
		// }
		// });

		// // get list items from strings.xml
		// drawerListViewItems =
		// getResources().getStringArray(R.array.planets_array);
		//
		// // get ListView defined in activity_main.xml
		// drawerListView = (ListView) findViewById(R.id.left_drawer);
		//
		// // Set the adapter for the list view
		// drawerListView.setAdapter(new ArrayAdapter<String>(this,
		// R.layout.drawer_list_item, drawerListViewItems));
		//
		// ActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout,
		// int drawerImageRes, int openDrawerContentDescRes, int
		// closeDrawerContentDescRes)
		//
		// DrawerLayout.setDrawerListener(ActionBarDrawerToggle)
		//
		// getActionBar().setDisplayHomeAsUpEnabled(true);

		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is
		// no hierarchical
		// parent.
		actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener
		// for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// When swiping between different app sections, select
						// the corresponding tab.
						// We can also use ActionBar.Tab#select() to do this if
						// we have a reference to the
						// Tab.
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter.
			// Also specify this Activity object, which implements the
			// TabListener interface, as the
			// listener for when this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				// The first section of the app is the most interesting -- it
				// offers
				// a launchpad into the other demonstrations in this example
				// application.
				return new LaunchpadSectionFragment();

			default:
				// The other sections of the app are dummy placeholders.
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
				fragment.setArguments(args);
				return fragment;
			}
		}

		@Override
		public int getCount() {
			return 4; // changes amount of sections
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "Section " + (position + 1);
		}
	}

	/**
	 * A fragment that launches other parts of the demo application.
	 */
	public static class LaunchpadSectionFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_section_launchpad, container, false);

			// Demonstration of a collection-browsing activity.
			rootView.findViewById(R.id.demo_collection_button)
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent intent = new Intent(getActivity(),
									CollectionDemoActivity.class);
							startActivity(intent);
						}
					});

			// Demonstration of navigating to external activities.
			rootView.findViewById(R.id.demo_external_activity)
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// Create an intent that asks the user to pick a
							// photo, but using
							// FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that
							// relaunching
							// the application from the device home screen does
							// not return
							// to the external activity.
							Intent intent = new Intent(getActivity(),
									MainActivity.class);
							startActivity(intent);
						}
					});

			return rootView;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		int activityCount = 0;

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_section_dummy,
					container, false);
			Bundle args = getArguments();
			((TextView) rootView.findViewById(android.R.id.text1))
					.setText(getString(R.string.dummy_section_text,
							args.getInt(ARG_SECTION_NUMBER)));
			((Button) rootView.findViewById(R.id.unsuperButton))
					.setBackgroundColor(Color.GREEN);
			Button button = (Button) rootView.findViewById(R.id.unsuperButton);
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					activityCount++;
				}
			});

			return rootView;
		}
	}

}