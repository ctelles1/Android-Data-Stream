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

public class RithmNameArrayAdapterToRecognition extends ArrayAdapter<RithmNameToStringStorage> {
	Context context;
	int layoutResourceId;
	ArrayList<RithmNameToStringStorage> data = new ArrayList<RithmNameToStringStorage>();
	List<EditText> editTextList = new ArrayList<EditText>();

	public RithmNameArrayAdapterToRecognition(Context context, int layoutResourceId,
			ArrayList<RithmNameToStringStorage> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		UserHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new UserHolder();
			holder.textRithm = (TextView) row.findViewById(R.id.rithmName);
			row.setTag(holder);
		} else {
			holder = (UserHolder) row.getTag();
		}
		RithmNameToStringStorage rithm = data.get(position);
		holder.textRithm.setText(rithm.getName());
		return row;

	}

	public EditText editText(String hint) {
		EditText editText = new EditText(context);
		editText.setId(Integer.valueOf(hint));
		editText.setHint(hint);
		editTextList.add(editText);
		return editText;
	}

	static class UserHolder {
		TextView textRithm;

	}
}
