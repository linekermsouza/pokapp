package br.com.uarini.pogapp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.uarini.pogapp.R;

/**
 * Created by marcos on 28/08/16.
 */
public class MyNumberPicker extends LinearLayout implements View.OnClickListener{

    private final int RM = R.id.ib_rm;
    private final int ADD = R.id.ib_add;

    private TextView tvValue;

    private int value = 0;

    private OnValueChangedListener valueListener;

    public MyNumberPicker(Context context) {
        super(context);
    }

    public MyNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyNumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(){
        if ( this.isInEditMode() )
            return;

        this.findViewById(RM).setOnClickListener(this);
        this.findViewById(ADD).setOnClickListener(this);
        this.tvValue = (TextView) this.findViewById(R.id.tv_value);
        this.updateViewValue();
    }

    @Override
    public void onClick(View view) {
        if ( view.getId() == RM ) {
            if ( this.value == 0 ){
                return;
            }

            if (this.notifyValueChanged(value-1)) {
                this.value--;
                this.updateViewValue();
            }
        } else if ( view.getId() == ADD ){
            if (this.value == 1000)
                return;
            if (this.notifyValueChanged(value+1)) {
                this.value++;
                this.updateViewValue();
            }

        }
    }

    private boolean notifyValueChanged(int newValue){
        if ( this.valueListener != null ){
            return this.valueListener.onValue(this, newValue);
        }
        return false;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        this.updateViewValue();
    }

    public void updateViewValue() {
        this.tvValue.setText(String.valueOf(value));
    }
    public void setValueListener(OnValueChangedListener valueListener) {
        this.valueListener = valueListener;
    }

    public static interface OnValueChangedListener {
        boolean onValue(MyNumberPicker picker, int value);
    }
}
