package step.wallet.maganger.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import step.wallet.maganger.R;
import step.wallet.maganger.data.InfoRepository;

public class DialogNotesTransaction extends DialogFragment {

    private static final String TAG = "DialogFragmentNotes";

    public interface OnInputSend {
        void sendNotes(String sendNotes);
    }

    public OnInputSend mOnInputSend;

    private Button notesOkBtn;
    private ImageButton notesCancelBtn;
    private EditText notesEt;

    private String getCurrentNotes;

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_tr_notes, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        notesOkBtn = view.findViewById(R.id.trNotesOk);
        notesCancelBtn = view.findViewById(R.id.trNotesCancel);
        notesEt = view.findViewById(R.id.trEtNotes);
        notesEt.setText(getCurrentNotes);


        notesOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notesEt.getText().toString().equals(""))
                    mOnInputSend.sendNotes("Notatki...");
                mOnInputSend.sendNotes(notesEt.getText().toString());
                getDialog().dismiss();
            }
        });

        notesCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputSend
                    = (OnInputSend) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: "
                    + e.getMessage());
        }
    }

    public void setValue(String notes) {
        this.getCurrentNotes = notes;
    }

//    public void sendData() {
//        mOnInputSelected.sendInput(categoryName, imageResource);
//        Toast.makeText(getContext(), "Send data", Toast.LENGTH_SHORT).show();
//    }

}
