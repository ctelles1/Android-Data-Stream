package rithmio.learn.test;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RithmNameArrayAdapterToEdit extends
		ArrayAdapter<RithmNameToStringStorage> {

	Context context;
	int layoutResourceId;
	ArrayList<RithmNameToStringStorage> data = new ArrayList<RithmNameToStringStorage>();
	List<EditText> editTextList = new ArrayList<EditText>();
	EditRithmNameFragment rithmArray;

	/**
	 * @param context
	 * @param layoutResourceId
	 * @param data
	 */
	public RithmNameArrayAdapterToEdit(Context context, int layoutResourceId,
			ArrayList<RithmNameToStringStorage> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RithmHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new RithmHolder();
			holder.textRithm = (TextView) row.findViewById(R.id.rithmName);
			holder.btnEdit = (Button) row.findViewById(R.id.editButton);
			holder.btnDelete = (Button) row.findViewById(R.id.deleteButton);
			row.setTag(holder);
		} else {
			holder = (RithmHolder) row.getTag();
		}
		RithmNameToStringStorage rithm = data.get(position);
		holder.textRithm.setText(rithm.getName());
		holder.btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("Edit Button Clicked", "**********");
				Toast.makeText(context, "Edit button Clicked",
						Toast.LENGTH_SHORT).show();

				// StringBuilder stringBuilder = new StringBuilder();
				// for (EditText editText : editTextList) {
				// stringBuilder.append(editText.getText().toString());
				// }

				// rithmArray.add(new Rithm("Rithm 3"));

			}
		});
		holder.btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("Delete Button Clicked", "**********");
				Toast.makeText(context, "Delete button Clicked",
						Toast.LENGTH_SHORT).show();

			}
		});
		return row;

	}

	/**
	 * To create an EditText which handles the Rithm Name
	 * 
	 * @param hint
	 * @return
	 */
	// public EditText editText(String hint) {
	// EditText editText = new EditText(context);
	// editText.setId(Integer.valueOf(hint));
	// editText.setHint(hint);
	// editTextList.add(editText);
	// return editText;
	// }

	static class RithmHolder {
		TextView textRithm;
		Button btnEdit;
		Button btnDelete;
	}
}
