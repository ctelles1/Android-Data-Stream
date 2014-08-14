package rithmio.learn.test;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class RecognitionFragment extends Fragment {

	ListView rithmList;
	RithmCustomAdapterRecog rithmAdapter;
	ArrayList<Rithm> rithmArray = new ArrayList<Rithm>();
	View recog;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/**
		 * add item in arraylist
		 */
		rithmArray.add(new Rithm("Rithm 1"));
		rithmArray.add(new Rithm("Rithm 2"));

		/**
		 * set item into adapter
		 */
		rithmAdapter = new RithmCustomAdapterRecog(getActivity(),
				R.layout.current_row_recog, rithmArray);
		rithmList = (ListView) recog.findViewById(android.R.id.list);
		rithmList.setItemsCanFocus(false);
		rithmList.setAdapter(rithmAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		recog = inflater.inflate(R.layout.recognition, container, false);

		return recog;
	}

}