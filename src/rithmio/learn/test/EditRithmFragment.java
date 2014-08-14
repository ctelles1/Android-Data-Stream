package rithmio.learn.test;

import java.util.ArrayList;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class EditRithmFragment extends ListFragment implements
		OnItemClickListener {

	ListView rithmList;
	RithmCustomAdapter rithmAdapter;
	ArrayList<Rithm> rithmArray = new ArrayList<Rithm>();
	View edit;
	EditText editName = (EditText) edit.findViewById(R.id.editName);
	Button btnEdit = (Button) edit.findViewById(R.id.button1);

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
		rithmEditor();

	}

	public void rithmEditor() {
		rithmAdapter = new RithmCustomAdapter(getActivity(),
				R.layout.current_row_edit, rithmArray);
		rithmList = (ListView) edit.findViewById(android.R.id.list);
		rithmList.setItemsCanFocus(false);
		rithmList.setAdapter(rithmAdapter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		edit = inflater.inflate(R.layout.editing, container, false);
		return edit;
	}

	public void onItemClick(AdapterView<?> parent, View edit, int position,
			long id) {
		position += 1; // So the count starts at Rithm 1, not Rithm 0
		Toast.makeText(getActivity(), "Rithm " + position, Toast.LENGTH_SHORT)
				.show();
		rithmArray.add(new Rithm("Rithm2 " + position));
		rithmEditor();
	}

//	public void editClick(View edit) {
//		editName.setVisibility(View.VISIBLE);
//	}

}