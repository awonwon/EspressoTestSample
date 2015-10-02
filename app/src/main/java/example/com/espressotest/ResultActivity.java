package example.com.espressotest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by GTW on 2015/10/1.
 */
public class ResultActivity extends AppCompatActivity{
    Button btnSummit;
    EditText editText;
    final

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        btnSummit = (Button) findViewById(R.id.btnSummit);
        editText = (EditText) findViewById(R.id.editText);

        btnSummit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                intent.putExtra("msg", editText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
