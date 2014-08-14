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
	RithmNameArrayAdapterToRecognition rithmAdapter;
	ArrayList<RithmNameToStringStorage> rithmArray = new ArrayList<RithmNameToStringStorage>();
	View recog;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// TODO Delete this list, make dynamic list adder, called from
		// Supervised Learning and Unsupervised Learning fragments after Rithm
		// is learned
		rithmArray.add(new RithmNameToStringStorage("Rithm 1"));
		rithmArray.add(new RithmNameToStringStorage("Rithm 2"));
		rithmArray.add(new RithmNameToStringStorage("Rithm 3"));
		rithmArray.add(new RithmNameToStringStorage("Rithm 4"));
		rithmArray.add(new RithmNameToStringStorage("Rithm 5"));
		rithmArray.add(new RithmNameToStringStorage("Rithm 6"));
		rithmArray.add(new RithmNameToStringStorage("Rithm 7"));
		rithmArray.add(new RithmNameToStringStorage("Rithm 8"));
		rithmArray.add(new RithmNameToStringStorage("Rithm 9"));
		rithmArray.add(new RithmNameToStringStorage("Rithm 10"));
		rithmArray.add(new RithmNameToStringStorage("Rithm 11"));
		rithmArray.add(new RithmNameToStringStorage("Rithm 12"));

		/**
		 * set item into adapter
		 */
		rithmAdapter = new RithmNameArrayAdapterToRecognition(getActivity(),
				R.layout.rithm_recognition_row, rithmArray);
		rithmList = (ListView) recog.findViewById(android.R.id.list);
		rithmList.setItemsCanFocus(false);
		rithmList.setAdapter(rithmAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return recog = inflater.inflate(R.layout.recognition, container, false);
	}

}