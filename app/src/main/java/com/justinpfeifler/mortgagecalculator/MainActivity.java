package com.justinpfeifler.mortgagecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.util.Log;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance();
    private static final NumberFormat PERCENT_FORMAT = NumberFormat.getPercentInstance();

    private double purchasePrice = 0.0;
    private double downPayment = 0.0;
    private double interestRate = 0.0;
    private double monthlyPayment = 0.0;
    private double totalPayment = 0.0;
    private int years = 15;
    private TextView purchasePriceTextView;
    private TextView downPaymentTextView;
    private TextView interestRateTextView;
    private TextView yearsTextView;
    private TextView monthlyPaymentTextView;
    private TextView totalPaymentTextView;

    private final SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean front) {
            years = progress;
            calculate();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private final TextWatcher purchasePriceTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                purchasePrice = Double.parseDouble(s.toString()) / 100.0;
                purchasePriceTextView.setText(CURRENCY_FORMAT.format(purchasePrice));
            } catch (NumberFormatException nFEx) {
                purchasePriceTextView.setText("");
                purchasePrice = 0.0;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            calculate();
        }
    };

    private final TextWatcher downPaymentTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                downPayment = Double.parseDouble(s.toString()) / 100.0;
                downPaymentTextView.setText(CURRENCY_FORMAT.format(downPayment));
            } catch (NumberFormatException nFEx) {
                downPaymentTextView.setText("");
                downPayment = 0.0;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            calculate();
        }
    };

    private final TextWatcher interestRateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                interestRate = Double.parseDouble(s.toString()) / 100.0;
                interestRateTextView.setText(PERCENT_FORMAT.format(interestRate));
            } catch (NumberFormatException nFEx) {
                interestRateTextView.setText("");
                interestRate = 0.0;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            calculate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.purchasePriceTextView = (TextView) findViewById(R.id.purchase_price_text_view);
        this.downPaymentTextView = (TextView) findViewById(R.id.down_payment_text_view);
        this.interestRateTextView = (TextView) findViewById(R.id.interest_rate_text_view);
        this.yearsTextView = (TextView) findViewById(R.id.years_text_view);
        this.monthlyPaymentTextView = (TextView) findViewById(R.id.monthly_payment_text_view);
        this.totalPaymentTextView = (TextView) findViewById(R.id.total_payment_text_view);

        EditText purchasePriceEditText = (EditText) findViewById(R.id.purchase_price_edit_text);
        EditText downPaymentEditText = (EditText) findViewById(R.id.down_payment_edit_text);
        EditText interestRateEditText = (EditText) findViewById(R.id.interest_rate_edit_text);

        purchasePriceEditText.addTextChangedListener(this.purchasePriceTextWatcher);
        downPaymentEditText.addTextChangedListener(this.downPaymentTextWatcher);
        interestRateEditText.addTextChangedListener(this.interestRateTextWatcher);

        SeekBar yearsSeekBar = findViewById(R.id.years_seek_bar);
        yearsSeekBar.setOnSeekBarChangeListener(this.seekBarChangeListener);

        calculate();
    }

    private void calculate() {

        // update the seekbar with the years
        this.yearsTextView.setText(String.valueOf(this.years) + " Year(s)");

        // calculate the monthly payment
        double loanAmount = this.purchasePrice - this.downPayment;
        double termInMonths = this.years * 12;
        double monthlyInterestRate = interestRate / 12;

        monthlyPayment = (loanAmount * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -termInMonths));

        // calculate the total payment
        totalPayment = this.monthlyPayment * (this.years * 12);

        // update the monthly payment and total payment views with new amounts
        this.monthlyPaymentTextView.setText(CURRENCY_FORMAT.format(monthlyPayment));
        this.totalPaymentTextView.setText(CURRENCY_FORMAT.format(totalPayment));
    }
}
