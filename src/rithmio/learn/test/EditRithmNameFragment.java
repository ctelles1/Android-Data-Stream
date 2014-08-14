package rithmio.learn.test;

import java.util.ArrayList;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class EditRithmNameFragment extends ListFragment {

	ListView rithmList;
	RithmNameArrayAdapterToEdit rithmAdapter;
	ArrayList<RithmNameToStringStorage> rithmArray = new ArrayList<RithmNameToStringStorage>();
	View edit;

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
		rithmAdapter = new RithmNameArrayAdapterToEdit(getActivity(),
				R.layout.rithm_edit_row, rithmArray);
		rithmList = (ListView) edit.findViewById(android.R.id.list);
		rithmList.setItemsCanFocus(false);
		rithmList.setAdapter(rithmAdapter);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return edit = inflater.inflate(R.layout.rithm_name_editdelete, container, false);
	}

	// TODO onClick edit and delete - is it here or MainActivity?
	// TODO edit opens up keyboard and enters typed entry
	// TODO delete removes the Rithm from the list, shifts other Rithms up

}