package rithmio.learn.test;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditLearnedRithms extends Fragment {

	int i = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.rithm_editing, container, false);

	}

//	public void addEdit(View view) {
//		// EditText newRithm = (EditText) view.findViewById(R.id.rithm_editing);
//		i++;
//		MainMenu.onBackPressed();
//	}

}