package com.pigdogbay.weightrecorder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.pigdogbay.weightrecorder.model.IUnitConverter;
import com.pigdogbay.weightrecorder.model.Reading;
import com.pigdogbay.weightrecorder.model.UnitConverterFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EditFragment extends Fragment
{
	protected static final int RESULT_WEIGHT_SPEECH = 1;
	protected static final int RESULT_COMMENT_SPEECH = 2;

	double _Weight = 90.0D;
	Button _EditWeightButton;

	private EditText _EditTextComment;
	private DatePicker _DatePicker;
	private IUnitConverter _WeightConverter = UnitConverterFactory.create(UnitConverterFactory.KILOGRAMS_TO_KILOGRAMS); 

	public void setWeightConvert(IUnitConverter weightConverter)
	{
		_WeightConverter = weightConverter;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.edit_fragment, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// Views and contexts have now been created
		super.onActivityCreated(savedInstanceState);
		Activity activity = getActivity();
		_EditTextComment = (EditText) activity
				.findViewById(R.id.EditFragmentComment);
		_DatePicker = (DatePicker) activity
				.findViewById(R.id.EditFragmentDatePicker);
		((ImageButton) activity.findViewById(R.id.EditFragmentCommentSpeak))
				.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						startCommentSpeechToText();

					}
				});
		((Button) activity.findViewById(R.id.EditFragmentWeightPickerMinus))
				.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						minus();
					}
				});
		((Button) activity.findViewById(R.id.EditFragmentWeightPickerPlus))
				.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						plus();
					}
				});
		_EditWeightButton = (Button) activity
				.findViewById(R.id.EditFragmentWeightPickerEdit);
		_EditWeightButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				edit();
			}
		});
	}

	public void setReading(Reading reading)
	{
		_EditTextComment.setText(reading.getComment());
		_Weight = _WeightConverter.convert(reading.getWeight());
		updateText();
		Calendar cal = Calendar.getInstance();
		cal.setTime(reading.getDate());
		_DatePicker.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
	}

	public Reading getReading()
	{
		Reading reading = new Reading();
		// convert to kilograms
		double weight = _WeightConverter.inverse(_Weight);
		reading.setWeight(weight);
		reading.setDate(getDateTime());
		reading.setComment(_EditTextComment.getText().toString());
		return reading;
	}

	private Date getDateTime()
	{
		Calendar cal = Calendar.getInstance();
		int year = _DatePicker.getYear();
		int month = _DatePicker.getMonth();
		int day = _DatePicker.getDayOfMonth();
		cal.set(year, month, day);
		return cal.getTime();
	}

	public void hideKeyboard()
	{
		try
		{
			Context context = getActivity();
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null)
			{
				imm.hideSoftInputFromWindow(_EditTextComment.getWindowToken(),
						0);
			}
		}
		catch (Exception e)
		{
		}
	}

	private void startCommentSpeechToText()
	{
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
				getString(R.string.edit_speak_prompt_comment));
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
		try
		{
			startActivityForResult(intent, RESULT_COMMENT_SPEECH);
			_EditTextComment.setText("");
		}
		catch (ActivityNotFoundException a)
		{
			Toast.makeText(getActivity(), getString(R.string.edit_no_speech),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK || null == data)
		{
			return;
		}
		List<String> text = data
				.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
		if (text.size() == 0)
		{
			return;
		}
		switch (requestCode)
		{
		case RESULT_COMMENT_SPEECH:
			_EditTextComment.setText(text.get(0));
			break;
		}
	}

	public void setWeight(String weight)
	{
		try
		{
			_Weight = Double.parseDouble(weight);
		}
		catch (NumberFormatException ex)
		{
		}
		updateText();
	}

	public String getWeight()
	{
		return String.format(Locale.US, "%.1f", _Weight);
	}

	private void minus()
	{
		_Weight = _Weight - _WeightConverter.getStepIncrement();
		updateText();
	}

	private void plus()
	{
		_Weight = _Weight + _WeightConverter.getStepIncrement();
		updateText();
	}

	private void edit()
	{
		final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		final EditText input = new EditText(getActivity());
		input.setText(String.format(Locale.US, "%.1f", _Weight));
		input.setHint(String.format("%s (%s)",
				getString(R.string.edit_weight_hint), _WeightConverter.getUnits()));
		input.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_DECIMAL);
		alert.setTitle("Enter Weight");
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				try
				{
					String value = input.getText().toString().trim();
					EditFragment.this._Weight = Double.parseDouble(value);
					EditFragment.this.updateText();
				}
				catch (NumberFormatException ex)
				{
				}
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				dialog.cancel();
			}
		});
		alert.show();
	}

	private void updateText()
	{
		String text = _WeightConverter.getDisplayString(_Weight);
		_EditWeightButton.setText(text);
	}

}
