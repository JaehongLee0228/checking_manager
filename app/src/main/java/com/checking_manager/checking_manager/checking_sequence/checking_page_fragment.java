package com.checking_manager.checking_manager.checking_sequence;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.checking_manager.checking_manager.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link checking_page_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class checking_page_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int pageIdx;
    TextView quesText;

    EditText editText;
    Button correctionBtn;
    Button readyBtn;

    RadioGroup radioGroup;


    public checking_page_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment checking_page_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static checking_page_fragment newInstance(String param1, String param2) {
        checking_page_fragment fragment = new checking_page_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_checking_page_fragment, container, false);

        Bundle extra = getArguments();
        String question = extra.getString("question");
        pageIdx = extra.getInt("index");

        quesText = rootView.findViewById(R.id.checkingPageQuestion);
        quesText.setText(question);

        editText = (EditText)rootView.findViewById(R.id.checkingPageEditText);
        correctionBtn = (Button)rootView.findViewById(R.id.checkingPageCorrectBtn);
        readyBtn = (Button)rootView.findViewById(R.id.checkingPageReadyBtn);

        correctionBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                editText.setFocusableInTouchMode(true);
                editText.setClickable(true);
                editText.setFocusable(true);
            }
        });

        readyBtn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                editText.setClickable(false);
                editText.setFocusable(false);
                String tmp = editText.getText().toString();

                checking_page2.specialNote[pageIdx] = tmp;
            }
        });

        radioGroup = rootView.findViewById(R.id.checkingPageRadioGroup);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);
        return rootView;
    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if(checkedId == R.id.checkingPageBtn1){
                checking_page2.statusResult[pageIdx] = "좋음";
            }
            else if(checkedId == R.id.checkingPageBtn2){
                checking_page2.statusResult[pageIdx] = "보통";
            }
            else if(checkedId == R.id.checkingPageBtn3){
                checking_page2.statusResult[pageIdx] = "나쁨";
            }
        }
    };


}