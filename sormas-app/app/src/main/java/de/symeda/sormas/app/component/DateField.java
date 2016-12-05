package de.symeda.sormas.app.component;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Date;

import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.app.R;

/**
 * Created by Mate Strysewske on 29.11.2016.
 */

public class DateField extends PropertyField<Date> implements DateFieldInterface {

    private TextView dateCaption;
    private EditText dateContent;
    private Date date;

    private InverseBindingListener inverseBindingListener;

    public DateField(Context context) {
        super(context);
        initializeViews(context);
    }

    public DateField(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public DateField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeViews(context);
    }

    @Override
    public void setValue(Date value) {
        date = value;
        if(value != null) {
            dateContent.setText(DateHelper.formatDDMMYYYY(value));
        }
    }

    @Override
    public Date getValue() {
        return date;
    }

    @BindingAdapter("android:value")
    public static void setValue(DateField view, Date date) {
        view.setValue(date);
    }

    @InverseBindingAdapter(attribute = "android:value", event = "android:valueAttrChanged" /*default - can also be removed*/)
    public static Date getValue(DateField view) {
        return view.getValue();
    }

    @BindingAdapter("android:valueAttrChanged")
    public static void setListener(DateField view, InverseBindingListener listener) {
        view.inverseBindingListener = listener;
    }

    public void setInputType(int type) {
        dateContent.setInputType(type);
    }

    public void setOnClickListener(OnClickListener listener) {
        dateContent.setOnClickListener(listener);
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        dateContent.setOnFocusChangeListener(listener);
    }

    /**
     * Inflates the views in the layout.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.field_date_field, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dateContent = (EditText) this.findViewById(R.id.date_content);
        dateContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                if (inverseBindingListener != null) {
                    inverseBindingListener.onChange();
                }
                onValueChanged();
            }
        });
        dateCaption = (TextView) this.findViewById(R.id.date_caption);
        dateCaption.setText(getCaption());
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        dateContent.setEnabled(enabled);
        dateCaption.setEnabled(enabled);
    }

}
